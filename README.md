**OLD
# YoutubeClone

Youtube clone made with Java for learning purposes. 

In this example the user can sign in, sign up, upload, download, delete, modify, and watch videos in "streaming".
This isn't a really good clone, but I learned a lot about comunications, sockets, encoding, ... so after all, was a really fun project to make.

# How it works

Users logs in the client and this sends a packet through a socket to the server who is listening, the packet sended, follows a custom standard (That you can change)
in this case for example the log in packet contains: 
"PROTOCOLCRISTOTUBE1.0#LOGIN#"+login+"#"+password;
and the server will respond:  "PROTOCOLCRISTOTUBE1.0#OK#USER_LOGGED#"+logdata; (in case that the log in is succesfull)

Afeter the log in the user can access to watch the other users videos, or upload his owns. For uploading or watching a video, I used BASE64 and cutted it in packets (following the standart: "PROTOCOLCRISTOTUBE1.0#OK#"+videoID+"#" + encodedString)

# 
This is just a learning project.


