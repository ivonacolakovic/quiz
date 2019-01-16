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
const app_ip = "192.168.1.108";

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

	db.query("SELECT * FROM vprasanje", (error, results, fields) => {
		if (error) {
			res.status(500);
			throw error;
		}

		// if no results == our db is empty
		if (results.length === 0) {
			let baseUrl;

			// get questions from each category with id = i
			for (let i in categoryIds) {
				// create url
				baseUrl = `https://opentdb.com/api.php?amount=50&category=${categoryIds[i]}`;

				// 2 sec timeout between each request
				setTimeout(async () => {
					// wait for response
					const data = await apiCallService(baseUrl);

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
						db.query(`SELECT idkategorija FROM kategorija WHERE kategorija = "${qd.category}";`, (error, cat_result) => {
							if (error) throw error;
							insertObj.fk_kategorija = cat_result[0];

							// get fk za tezavnost
							db.query(`SELECT idtezavnost FROM tezavnost WHERE tezavnost = "${qd.difficulty}";`, (error, diff_result) => {
								if (error) throw error;
								insertObj.fk_tezavnost = diff_result[0];

								// get fk for type
								db.query(`SELECT idtip_vprasanj FROM tip_vprasanj WHERE tip_vprasanj = "${qd.type}"`, (error, type_result) => {
									if (error) throw error;
									insertObj.fk_tip_vprasanj = type_result[0];

									const b = 2;
								});
							});
						});
					}
				}, 2000);
			}
		} else {
			// data is already in db
			const a = 1;
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

	for (let item in questions) {
		db.query(
			`
		SELECT * FROM vprasanje
		WHERE fk_tip_vprasanj = (
			SELECT idtip_vprasanj FROM tip_vprasanj
			WHERE tip_vprasanj = "${item}"
		) ORDER BY rand() LIMIT 10;
	`,
			(error, results) => {
				if (error) {
					console.log(`Error while selecting ${item} questions!`);
					res.status(500);
					throw error;
				}
				questions[item] = results;
			},
		);
	}
	res.status(200).send(questions);
});

/**
 * get questions for custom game based on
 * 1. tezavnost
 * 2. kategorija
 * 3. tip_vprasanj
 * url =	serverip:serverport/api/game/custom/query?tezavnost=hard&kategorija=sport&tip_vprasanj=multiple choice
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
		dbQuery += `${hasAnd ? " AND " : " "}${key} = ${parsedQs[key]}`;
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
const apiCallService = async (url) => {
	const res = await axios.get(url);
	const { data } = await res;
	return data;
};

// run app
app.listen(app_port, app_ip, () => {
	console.log(`\nSERVER LISTENING ON http://${app_ip}:${app_port}/\n`);
});
