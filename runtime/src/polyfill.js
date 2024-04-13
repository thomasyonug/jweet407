Object.prototype.wait = function () {
    Comm.wait(this)
}

Object.prototype.notify = function () {
    Comm.wait(this)
}