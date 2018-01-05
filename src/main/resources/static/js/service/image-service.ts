import * as Rx from "rxjs/Rx";

class ImageService {

    private processLink(aaa:string): string {
        let helperLink = document.createElement("a");
        helperLink.href = aaa;
        if (/.*facebook\.com$/.test(helperLink.hostname)) {
            let joinSign: string = aaa.indexOf("?") === -1 ? "?" : "&";
            return aaa + joinSign + "type=large"
        }
        return aaa;
    }

    findLargeImageLink(sourceLink: string): Rx.Observable<string>{
        return Rx.Observable.of(sourceLink)
                         .map(this.processLink)
    }
}

export const imageService = new ImageService();