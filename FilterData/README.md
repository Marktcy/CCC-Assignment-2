根据couchdb中5个city的数据</br>
此处添加6个文件存储the tweets from each city and the test file</br>
The data format: </br>
tweetID,twitterResource,coordinates,sentiment</br>

tweetID: 由双扩号扩起来</br>
twitterResource: 大致是iphone, android, ipad，还有一系列的第三方app，如Instagram，这边可以分类一下</br>
coordinates: [longitude, latitude]</br>
sentiment: POSITIVE, NEGATIVE, NEUTRAL 这三种感情中的一种，在java中使用的是enum，做统计的话，无需担心大小写等问题</br>
这四种数据，由逗号统一隔开，方便parse or regex.</br>

-----updated
filterADE.txt data format:
"642659303489445888",,Foursquare,"Sat Sep 12 11:21:32 +0000 2015",(138.600502,-34.85254),NEUTRAL
