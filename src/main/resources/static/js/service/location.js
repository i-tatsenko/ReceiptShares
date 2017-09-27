import Cookies from 'js-cookie'

const COOKIE_LOCATION_ALLOWED = "location-service-allowed";
const PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

class Location {

    getCurrentLocation(callback) {
        isLocationAccessAllowed(function (result) {
            if (result) {
                getLocation(callback);
            } else {
                throw Error("Not implemented alternative way")
            }
        })
    }

    getPlacesNearWithName(name, callback) {
        this.getCurrentLocation(function (result) {
            let data = {
                key: "AIzaSyDAs1kSd6gN_ef68tXv-HyrOr9ZHzTfd2A",
                location: result.coords.latitude + "," + result.coords.longitude,
                radius: 1000
            };
            if (name && name !== '') {
                data.keyword = name
            }
            $.get({
                url: PLACES_URL,
                data: data,
                success: callback,
                dataType: 'json'
            });
        })
    }

}

function isLocationAccessAllowed(callback) {
    if (!navigator.geolocation){
        callback(false);
        return;
    }
    let allowed = Cookies.get(COOKIE_LOCATION_ALLOWED);
    if (allowed === undefined) {
        askForAccess(result => {
            setAccessCookie(result);
            callback(result);
        })
    } else {
        callback(allowed);
    }
}

function getLocation(callback) {
    navigator.geolocation.getCurrentPosition(callback, error => console.log("Can't get current location", error));
}

function setAccessCookie(accessAllowed) {
    let cookiesExpireDays = accessAllowed ? 100 : 7;
    Cookies.set(COOKIE_LOCATION_ALLOWED, accessAllowed, cookiesExpireDays);
}

function askForAccess(callback) {
    navigator.permissions.query({name:'geolocation'}).then(result => {console.log(result);callback(result.state === 'granted')})
}

export default new Location()