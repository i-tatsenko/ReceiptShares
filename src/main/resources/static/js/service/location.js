import Cookies from 'js-cookie'

const COOKIE_LOCATION_ALLOWED = "location-service-allowed";
const PLACES_URL = "/v1/place/suggest";

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
            if (!name || name === '') {
                return
            }
            $.ajax({
                url: PLACES_URL,
                type: 'GET',
                data: {
                    query: name,
                    lat: result.lat,
                    long: result.long
                },
                success: callback

            });
        });
    }

}

function isLocationAccessAllowed(callback) {
    if (!navigator.geolocation) {
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
        callback(allowed === 'true');
    }
}

function getLocation(callback) {
    let lastLocationCookie = Cookies.get("last-location");
    if (lastLocationCookie) {
        callback(JSON.parse(lastLocationCookie));
    } else {
        let saveLocationAndReturn = function (location) {
            let result = {
                lat: location.coords.latitude,
                long: location.coords.longitude
            };
            console.log(result);
            Cookies.set('last-location', JSON.stringify(result), 5 * 60 * 1000);
            callback(result);
        };
        navigator.geolocation.getCurrentPosition(saveLocationAndReturn, error => console.log("Can't get current location", error));
    }
}

function setAccessCookie(accessAllowed) {
    let cookiesExpireDays = accessAllowed ? 100 : 7;
    Cookies.set(COOKIE_LOCATION_ALLOWED, accessAllowed, cookiesExpireDays);
}

function askForAccess(callback) {
    navigator.permissions.query({name: 'geolocation'}).then(result => {
        console.log(result);
        callback(result.state === 'granted' || result.state === 'prompt')
    })
}

export default new Location()