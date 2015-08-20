# This app is an assignment from “Programming Mobile Applications for Android Handheld Systems: Part 2” at Coursera.org #

An application that periodically reminds the user to take a selfie - a picture of one's self taken from one's device. Over time the user will capture many selfies and thus will be able to see him or herself change over some period of time.

If the user clicks on the camera icon on the ActionBar, the app will open up a picture taking app already installed on the device.

If the user now snaps a picture and accepts it, the picture is returned to the app and then displayed to the user along with other selfies the user may have already taken.

If the user clicks on the small view, then a large view will open up, showing the selfie in a larger format. Hitting the back button in this case brings the user back to the ListView.

Whenever the user takes a selfie, the image data is stored in some permanent way. In particular, if the user exits the app and then reopens it, they have access to all the selfies saved on their device.

Because the user wants to take selfies periodically over a long period of time, the app creates and sets an Alarm that fires roughly once every twenty seconds. In a real app, this would most likely be set to a longer period, such as once per day. We will fire the alarms roughly every two minutes to make assessment easier. When one of these alarms fires, a notification area notification should be placed in the notification area, as shown below.