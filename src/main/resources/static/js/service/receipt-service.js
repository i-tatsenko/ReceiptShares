import {Observable} from "rxjs/Observable";

class ReceiptService {
    addOneItem(receiptId, itemId) {
        Observable.fromPromise(
            $.post(`/v1/receipt/${receiptId}/item/${itemId}/increment`))
                  .catch((err, source) => {
                      console.log(err);
                      return source;
                  })
    }
}


export default new ReceiptService();