# MongoDB-Insert-issue
Issue:
The above mentioned code inserts document in different format.
"Retweeted_status" : [
        "status:#Damasco ha l'audio""
        "created_at:Tue Sep 27 23:09:45 BST 2016",
        "retweetCount:9",
        "Likes:6",
        "UniqueID_user:2870870995",
        "screen_name:danyace2014",
        "Name:Daniele Vance ?",
        "lang:it",
        "Status_count:11899",
        "Friends_count:7605",
        "Followers_count:7078",
        "Description:E'\nUna\nRapina\nOrganizzata\n\n#NoEuro",
        "LocationGEO:Varese",
        "TimeZone:null"
]

It should be like this...
"Retweeted_status" : [
        "status":"#Damasco ha l'audio",
        "created_at":"Tue Sep 27 23:09:45 BST 2016",
        "retweetCount":"9",
        "Likes":"6",
        "UniqueID_user":"2870870995",
        "screen_name":"danyace2014",
        "Name":"Daniele Vance ?",
        "lang":"it",
        "Status_count":"11899",
        "Friends_count":"7605",
        "Followers_count":"7078",
        "Description":"E'\nUna\nRapina\nOrganizzata\n\n#NoEuro",
        "LocationGEO":"Varese",
        "TimeZone":"null"
]

# Tweet file
Single tweet in json format
#Tweets_to_Mongodb.java
Java-mongoDB insertion code

