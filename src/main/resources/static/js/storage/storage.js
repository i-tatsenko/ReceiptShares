import {EventEmitter} from "fbemitter"

let state = {};
let emitter = new EventEmitter();

class Storage {

        setUser(user) {
            state.user = user
        };

        getState() {
            return $.extend(true, {}, state)
        };

        listenFor(propertyName, fn) {
            emitter.addListener(propertyName, fn)
        }

        screenTitle(newTitle) {
            state.screenTitle = newTitle;
            emitter.emit("screenTitle");
        }
}

export default new Storage();
