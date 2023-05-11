package com.example.Innovact.models

data class TopicModel (
    var topicId:String?= null,
    var topicTitle:String?= null,
    var topicDescription:String?= null,
    var comment:ArrayList<CommentModel>?= null,
)

