from flask import Blueprint, request, jsonify, session
from flask_pymongo import PyMongo
import bcrypt
import json
from bson.objectid import ObjectId
from database import mongo
import jwt
import datetime
from functools import wraps

police_side = Blueprint('police_side', __name__)

SECRET_KEY = <JWT SECRET KEY HERE>



@police_side.route('/api/register_police',methods=['POST'])
def register():
	try:

		polices = mongo.db.police
		existing_police = polices.find_one({'email':request.json['email']})

		if existing_police is None:
			hashpass = bcrypt.hashpw(request.json['password'].encode('utf-8'),bcrypt.gensalt())
			id = polices.insert({
				'name':request.json['name'],
				'email':request.json['email'],
				'phone':request.json['phone'],
				'password':hashpass
				})

			return jsonify({'id':str(id),'status':201})
		else:
			return jsonify({'id':"user exists!!",'status':401})

	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})
		


@police_side.route('/api/login_police',methods=['POST'])
def login():
	try:
		polices = mongo.db.police
		login_police = polices.find_one({'email':request.json['email']})

		if login_police:
			if login_police['password'] == bcrypt.hashpw(request.json['password'].encode('utf-8'),login_police['password']):
				login_police['_id']=str(login_police['_id'])
				token = jwt.encode({'uid':login_police['_id'],'exp' : datetime.datetime.utcnow() + datetime.timedelta(minutes=720)},SECRET_KEY)
				login_police['token']=token.decode('UTF-8')
				del login_police['password']
				return jsonify({'id':login_police,"status":200})
			else:
				return jsonify({'id':"password wrong","status":404})
		else:
			return jsonify({'id':"user not exists!!","status":403})
	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})



@police_side.route('/api/police/get_passes/<status>',methods=['GET'])
def get_passes(status):
	try:
		passes = mongo.db.passes
		all_passes=[]
		for p in passes.find({"status":int(status)}):
			p['_id']=str(p['_id'])
			all_passes.append(p)
		return jsonify({'id':all_passes,"status":200})
	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})




@police_side.route('/api/police/get_pass',methods=['POST'])
def get_pass():
	try:
		passes = mongo.db.passes

		p = passes.find_one({"_id":ObjectId(request.json['pid'])})
		if p:
			p['_id']= str(p['_id'])
			return jsonify({'id':p,"status":200})
		else:
			return jsonify({'id':"pass not exists!!","status":404})

	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})



@police_side.route('/api/police/validate_pass',methods=['PUT'])
def validate_pass():
	try:
		passes = mongo.db.passes

		passes.find_one_and_update({"_id":ObjectId(request.json['pid'])},
			{'$set':{'status':int(request.json['status'])}})

		#implement notification here

		return jsonify({'id':"updated",'status':200})
	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})




@police_side.route('/api/police/get_user',methods=['POST'])
def get_user():
	try:
		users = mongo.db.user

		user = users.find_one({"_id":ObjectId(request.json['uid'])})
		if user:
			user['_id']= str(user['_id'])
			del user['password']
			return jsonify({'id':user,"status":200})
		else:
			return jsonify({'id':"user not exists!!","status":404})

	except Exception as e:
		print(e)
		return jsonify({'id':"failed",'status':500})