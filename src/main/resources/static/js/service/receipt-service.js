import {Observable} from "rxjs/Rx";
import {httpService} from './http-service';

class ReceiptService {

    getAll() {
        return httpService.get('/v1/receipt/all');
    }

    getReceipt(id) {
        return httpService.get(`/v1/receipt/${id}`)
    }

    listenForReceiptChanges(id) {
        return Observable.create(obs => {
            let eventSource = new EventSource(`/v1/receipt/${id}/listen`);
            eventSource.onmessage = data => obs.next(data);

            return () => {console.log("Closing event source");eventSource.close();}
        })
    }

    addOneItem(receiptId, itemId) {
        return httpService.post(`/v1/receipt/${receiptId}/item/${itemId}/increment`);
    }

    deleteOneItem(receiptId, itemId) {
        return httpService.post(`/v1/receipt/${receiptId}/item/${itemId}/increment?amount=-1`);
    }

    cloneItem(receiptId, itemId) {
        return httpService.post(`/v1/receipt/${receiptId}/item/${itemId}/clone`);
    }

    undoDelete(receiptId, itemId) {
        return httpService.post(`/v1/receipt/${receiptId}/item/${itemId}/restore`);
    }
}


export const receiptService = new ReceiptService();