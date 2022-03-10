import dotenv from 'dotenv';

var NodeGeocoder = require('node-geocoder');

dotenv.config({
  path: '.env'
});

var options = {
  provider: 'google',
  httpAdapter: 'https', // Default
  apiKey: process.env.KEY_MAPS, // for Mapquest, OpenCage, Google Premier
  formatter: 'json' // 'gpx', 'string', ...
};

var geocoder = NodeGeocoder(options);

export const getAddressFromLatLong = async (latitude: number, longitude: number) => {
    var result = await geocoder.reverse({lat:latitude, lon:longitude})
    return result[0].formattedAddress;
}

