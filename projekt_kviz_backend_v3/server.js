/**
 * main entry file for server
 */

const express = require("express"); // minimalist js web framework for nodejs
const ip = require("ip"); // node ip module for network interface
const morgan = require("morgan"); // logger
const bodyParser = require("body-parser"); // request body parser
const mysql = require("mysql"); // mysql module
const axios = require("axios"); // send network requests
const url = require("url"); // url parsing
const querystring = require("querystring"); // parse search parameters from query in url

// define app
const app = express();

// request & response logger
app.use(morgan("dev"));

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }));

// parse application/json
app.use(bodyParser.json());

// define app port and ip
const app_port = 5555;
// const app_ip = ip.address();
const app_ip = "164.8.207.4";

// mysql db settings
const db = mysql.createConnection({
	host: "localhost",
	port: 3306,
	user: "root",
	password: "root1",
	database: "projekt_kviz",
	insecureAuth: true,
});

/**
 * ROUTING START ===========================================================================================
 */

/**
 * home
 */
app.get("/", (req, res) => {
	res.send("Hello world!");
});

/**
 * checks if the data is already in db
 * if true, response true
 * if false, get data from api
 */
app.get("/api/core/check_data", (req, res) => {
	const categoryIds = [9, 11, 12, 18, 21, 22];

	db.query("SELECT * FROM vprasanje", (error0, results, fields) => {
		if (error0) {
			res.status(500);
			throw error0;
		}

		// if no results == our db is empty
		if (results.length === 0) {
			let baseUrl;

			// get questions from each category with id = i
			var promises = categoryIds.map((item) => {
				return new Promise((resolve, reject) => {
					// create url
					baseUrl = `https://opentdb.com/api.php?amount=50&category=${item}`;

					// wait for response
					apiCallService(baseUrl)
						.then((data) => {
							// dereference from result
							const { results } = data;

							// insert each object from result to db
							for (let item in results) {
								const qd = results[item]; // qd = question data
								let insertObj = {
									fk_kategorija: null,
									fk_tip_vprasanj: null,
									fk_tezavnost: null,
									vprasanje: qd.question,
									pravilen_odgovor: qd.correct_answer,
									nepravilen_odgovor1: qd.incorrect_answers[0] ? qd.incorrect_answers[0] : null,
									nepravilen_odgovor2: qd.incorrect_answers[1] ? qd.incorrect_answers[1] : null,
									nepravilen_odgovor3: qd.incorrect_answers[2] ? qd.incorrect_answers[2] : null,
								};

								/**
								 * DISCLAIMER:
								 * THIS IS NOT THE RIGHT WAY! :D :D :D
								 */

								// get fk za kategorijo
								db.query(`SELECT idkategorija FROM kategorija WHERE kategorija = "${qd.category}";`, (error1, cat_result) => {
									if (error1) {
										reject(error1);
										throw error1;
									}
									insertObj.fk_kategorija = cat_result[0].idkategorija;

									// get fk za tezavnost
									db.query(`SELECT idtezavnost FROM tezavnost WHERE tezavnost = "${qd.difficulty}";`, (error2, diff_result) => {
										if (error2) {
											reject(error2);
											throw error2;
										}
										insertObj.fk_tezavnost = diff_result[0].idtezavnost;

										// get fk for type
										db.query(`SELECT idtip_vprasanj FROM tip_vprasanj WHERE tip_vprasanj = "${qd.type}";`, (error3, type_result) => {
											if (error3) {
												reject(error3);
												throw error3;
											}
											insertObj.fk_tip_vprasanj = type_result[0].idtip_vprasanj;

											db.query("INSERT INTO vprasanje SET ?", insertObj, function(error4, insert_result) {
												if (error4) {
													reject(error4);
													throw error4;
												}
												resolve();
											});
										});
									});
								});
							}
						})
						.catch((err) => {
							console.log(err);
							reject(err);
						});
				});
			});

			Promise.all(promises)
				.then(() => {
					res.status(200).send("Success!");
				})
				.catch((err) => {
					console.log(err);
					res.status(500).send(`Err ${err}`);
				});
		} else {
			// data is already in db
			res.status(200).send("Data is already in db!");
		}
	});
});

/**
 * get questions for a regular game
 */
app.get("/api/game/regular/get_questions", (req, res) => {
	let questions = {
		easy: [],
		medium: [],
		hard: [],
	};

	// object keys map => dobis nazaj vsak key v `questions` (easy, medium, hard)
	// map through each key and select corresponding `tezavnost`
	var promises = Object.keys(questions).map(function(item) {
		// for each key return a promise
		return new Promise(function(resolve, reject) {
			// run db query for each key in `questions`
			db.query(
				`
			SELECT * FROM vprasanje
			WHERE fk_tezavnost = (
				SELECT idtezavnost FROM tezavnost
				WHERE tezavnost LIKE "${item}"
			) ORDER BY rand() LIMIT 10;
			`,
				(error, results) => {
					if (error) {
						console.log(`Error while selecting ${item} questions!`);
						res.status(500);
						reject(error); // if the current query errors out, reject the promise
						throw error;
					}
					questions[item] = results;
					resolve(); // if the current query runs fine, resolve the promise
				},
			);
		});
	});

	// when all promises are fulfilled, send response back to client
	Promise.all(promises)
		.then(() => {
			res.status(200).send(questions);
		})
		.catch((err) => {
			// if not all promises fulfilled, send error 500 to client
			res.status(500).send([]);
		});
});

/**
 * get questions for custom game based on
 * 1. tezavnost
 * 2. kategorija
 * 3. tip_vprasanj
 * url =	serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sport&tip_vprasanj=multiple
 */
app.get("/api/game/custom/query", (req, res) => {
	// url + query
	const rawUrl = req.url;

	// parsed origin
	let parsedUrl = url.parse(rawUrl);

	// parsed url so we can get search query with keys and values
	let parsedQs = querystring.parse(parsedUrl.query);

	// base query
	let dbQuery = `SELECT * FROM vprasanje WHERE`;
	let hasAnd = false;

	for (let key in parsedQs) {
		dbQuery += `${hasAnd ? " AND " : " "}fk_${key} = (SELECT id${key} FROM ${key} WHERE ${key} LIKE "${parsedQs[key]}")`;
		hasAnd = true;
	}
	dbQuery += " ORDER BY rand() LIMIT 20;";

	db.query(dbQuery, (error, results) => {
		if (error) {
			res.status(500);
			throw error;
		}
		res.send(results);
	});
});

/**
 * ROUTING END ===========================================================================================
 */

/**
 * UTILS START
 */

/**
 *
 * @param {1} url api url
 * @param {2} method defaults to "get"
 * @param {3} params if method is post, then you can send params along
 */
const apiCallService = async (url, method = "get", params = {}) => {
	return new Promise(function(resolve, reject) {
		const _mthd = method.toUpperCase();
		if (_mthd === "GET") {
			axios
				.get(url)
				.then((res) => {
					resolve(res.data);
				})
				.catch((err) => {
					console.log(err);
					reject(err);
				});
		} else if (_mthd === "POST") {
			axios
				.post(url, params)
				.then((res) => {
					resolve(res.data);
				})
				.catch((err) => {
					console.log(err);
					reject(err);
				});
		} else {
			console.log("Method not implemented!");
			reject(err);
		}
	});
};

// run app
app.listen(app_port, app_ip, () => {
	console.log(`\nSERVER LISTENING ON http://${app_ip}:${app_port}/\n`);
});
