
# Tokopedia Android Test 2020

  

Screening project for Android Developer at Tokopedia. The tests are:

##### Algorithm Test:

- Counting minimum path sum

- Counting posibility on climbing stairs

- Counting depth of an oil reservoir

  

##### Android Test

- Filter products on a given JSON file

- Handling input for API and Google Maps *`(Please refer to the documentation below if Maps doesn't work)`*

Details of each test can be seen inside the app itself. This README.md only covers the second Android Test since it might need workarounds in it.

## Android Test - Maps

In summary, the app needs API from https://rapidapi.com and Google Map API key (the key will be deactivated when I received confirmation email). While getting the GMaps API key, I also restrict its use to this application only. Since debug SHA1 is different from one computer to another, I decided to make a release version of the app and register the SHA1 of the release version to the Google Console. 
**If** the map won't run as intended, please follow this step:

>Change the Build Variant from debug to release
![](https://live.staticflickr.com/65535/51108500732_68235f5ab1_o_d.png)

![]()