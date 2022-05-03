package uz.evkalipt.sevenmodullesson51.models

import java.io.Serializable

class ForPager : Serializable {

    var title:String? = null
    var writerList:List<Writer>? = null

    constructor(title: String?, writerList: List<Writer>?) {
        this.title = title
        this.writerList = writerList
    }

    constructor()


}