import {EventEmitter} from "fbemitter";

let historyStack = [];
let emitter = new EventEmitter();

class NavigationHistory {

    listen(callback) {
        return emitter.addListener("history", callback);
    }

    popHistorySilently() {
        return historyStack.pop();
    }

    pushHistory(returnLink) {
        historyStack.push(returnLink);
        emitter.emit("history", historyStack.slice(0))
    }

    removeFromHistory(returnLink) {
        historyStack = historyStack.filter(item => item !== returnLink);
        emitter.emit("history", historyStack.slice(0))
    }
}

export default new NavigationHistory();