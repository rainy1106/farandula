package com.mtc.utils
//'PENDING','PAID','ACCEPTED','REJECTED','READY','SERVED','COMPLETED'
enum class OrderType(val type: String) {
    NEWORDER("PENDING"),
    ACCEPTED("ACCEPTED"),
    UPCOMINGORDER("PENDING"),
    PAID("BILL PAID"),
    ONLYPAID("PAID"),
    REJECTED("REJECTED"),
    READY("READY"),
    SERVED("SERVED"),
    COMPLETED("COMPLETED")
}