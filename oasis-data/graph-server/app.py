from flask import Flask

# https://blog.csdn.net/qq_33528613/article/details/86602707
app = Flask(__name__)


@app.route('/papers')
def get_graph():
    pass


@app.route('/')
def hello():
    return 'hello'


if __name__ == '__main__':
    app.run()
