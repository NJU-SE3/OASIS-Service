from flask import Flask
from flask_restplus import Resource, Api

app = Flask(__name__)
api = Api(app)


class Hello(Resource):
    pass


if __name__ == '__main__':
    app.run()
