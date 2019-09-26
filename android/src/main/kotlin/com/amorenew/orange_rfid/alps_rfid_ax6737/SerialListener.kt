package com.amorenew.orange_rfid.alps_rfid_ax6737

interface SerialListener {
    fun onDataUpdated( idString:String)
}
