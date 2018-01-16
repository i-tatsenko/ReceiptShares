import {httpService} from './http-service';

class ReceiptService {

    getReceipt(id) {
        return httpService.get(`/v1/receipt/${id}`)
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