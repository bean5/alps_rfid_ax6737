package com.amorenew.orange_rfid.alps_rfid_ax6737

/**
 * Goods
 * @author Administrator
 */
class TagDataMode {

    var barcodeid: Int = 0
    var name: String? = null      //
    var barcode: String? = null   //
    var state: String? = null   //
    var count: Int = 0          //

    constructor(name: String, barcode: String, count: Int) : super() {
        this.name = name
        this.barcode = barcode
        this.count = count
    }

    constructor() : super() {}

    override fun toString(): String {
        // TODO Auto-generated method stub
        return "��Ʒ����" + this.name + ",���룺 " + this.barcode
    }

}
