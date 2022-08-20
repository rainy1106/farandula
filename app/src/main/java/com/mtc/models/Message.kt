package com.mtc.models


class Message {

    var message: String? = null
    var senderId: String? = null
    var messageFrom: String? = null
    var seatId: String? = null
    var tableId: String? = null
    var currentDateTime: String? = null
    var childrenCount: String? = "0"
    var readChat: Boolean = false
    var pushKey: String? = null

    constructor()
    constructor(
        messageFrom: String, message: String,
        senderId: String, tableId: String, seatId: String, currentDateTime: String, readChat: Boolean,
        pushKey:String
    ) {
        this.message = message
        this.senderId = senderId
        this.messageFrom = messageFrom
        this.tableId = tableId
        this.seatId = seatId
        this.currentDateTime = currentDateTime
        this.readChat = readChat
        this.pushKey = pushKey
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true
        if (obj !is Message) {
            return false
        }
        val message: Message = obj
        return message.seatId.equals(seatId)
    }

    override fun hashCode(): Int {
        return seatId!!.toInt()
    }
}