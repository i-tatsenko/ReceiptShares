import {EventEmitter} from "fbemitter"
import * as Rx from "rxjs-es";

let state = {
    actionButtonMenuItems: []
};

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

    addAddActionButtonMenuItem(menuItem) {
        state.actionButtonMenuItems.push(menuItem);
        emitter.emit("add-action-menu");
    }

    removeAddActionButtonMenuItem(menuItem) {
        let indexToRemove = state.actionButtonMenuItems.indexOf(menuItem);
        state.actionButtonMenuItems.splice(indexToRemove, 1);
        emitter.emit("add-action-menu");
    }
}

class InviteService {

    findById(inviteId) {
        return Rx.Observable.fromPromise(
            $.get(`/v1/invite/${inviteId}`)
        )
    }
}

export const inviteService = new InviteService();

export default new Storage();
