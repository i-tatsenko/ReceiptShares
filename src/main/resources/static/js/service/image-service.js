import {Observable} from "rxjs/Observable"

function findLargeImageLink(link) {
    let helperLink = document.createElement("a");
    helperLink.href = link;
    if (helperLink.hostname.endsWith("facebook.com")) {
        let joinSign = link.indexOf("?") === -1 ? "?" : "&";
        return link + joinSign + "type=large"
    }
    return link;
}

class ImageService {

    findLargeImageLink(sourceLink) {
        return Observable.of(sourceLink)
                         .map(findLargeImageLink)
    }
}

export const imageService = new ImageService();