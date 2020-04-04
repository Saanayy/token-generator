from flask import Flask, request, jsonify, session
import bcrypt
import json
from bson.objectid import ObjectId
from database import mongo
import os

app = Flask("__name__")

#database config starts

app.config["MONGO_DBNAME"]= <Enter your db name here>
app.config["MONGO_URI"]= <Enter your mongo URI here>

mongo.init_app(app)

#database config ends

#user usecases start

from user_side import user_side
app.register_blueprint(user_side)

#user usecases ends


#police usecases starts

from police_side import police_side
app.register_blueprint(police_side)

#police usecases ends

if __name__ == '__main__':
	app.run(host='0.0.0.0',port=os.environ.get('PORT', '5000'),debug=True)