/**
 * main entry file for server
 */

const express = require("express"); // minimalist js web framework for nodejs
const ip = require("ip"); // node ip module for network interface
const morgan = require("morgan"); // logger
const bodyParser = require("body-parser"); // request body parser
const mysql = require("mysql");

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
const app_ip = ip.address();
//const app_ip = "192.168.1.107";

// mysql db settings
const db = mysql.createConnection({
	host: "localhost",
	port: 3306,
	user: "root",
	password: "root1",
	database: "projekt_kviz",
	insecureAuth: true,
});

// routing
app.get("/", (req, res) => {
	res.json({ asd: "ola" });
});

app.get("/getAllCategories", (req, res) => {
	db.connect();
	db.query("SELECT * FROM kategorija", (error, results, fields) => {
		if (error) throw error;
		res.status(200).send(fields);
	});
	db.end();
});

app.get("/:diff/:cat/:type", (req, res) => {
	const { diff, cat, type } = req.params;
	if (diff === "hard") {
	}
});

// run app
app.listen(app_port, app_ip, () => {
	console.log(`\nSERVER LISTENING ON http://${app_ip}:${app_port}/\n`);
});
