var requests = require('config/requests');
var request = require('request');


module.exports = function(app) {



	app.get('/', function(req, res) {

		res.end("Node-Android-Gift-Project"); 
	});


	app.post('/login',function(req,res){
		var email = req.body.email;
       		var password = req.body.password;
        	var reg_id = req.body.reg_id;
			var account = req.body.account;
			
			
		requests.login(email,password,reg_id,account,function (found) {
			console.log(found);
			res.json(found);
	});		
	});
	
	app.post('/send',function(req,res){
		var fromn = req.body.fromn;
		var name = req.body.name;
		var message = req.body.message;
		var gift_id = req.body.gift_id;
		requests.send(fromn,name,message,gift_id,function (found) {
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/getuser',function(req,res){
		var email = req.body.email;
			
		requests.getuser(email,function (found) {
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/logout',function(req,res){
		var email = req.body.email;

			
		requests.removeuser(email,function (found) {
			console.log(found);
			res.json(found);
	});		
	});

	
};



