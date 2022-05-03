package uz.evkalipt.sevenmodullesson51.models

class Writer {

    var imageUrl:String? = null
    var fullName:String? = null
    var yearOfBirth:String? = null
    var yearOfBeath:String? = null
    var type:String? = null
    var fullText:String? = null
    var save:Int? = null
    var uid:String? = null



    constructor()
    constructor(
        imageUrl: String?,
        fullName: String?,
        yearOfBirth: String?,
        yearOfBeath: String?,
        type: String?,
        fullText: String?,
        save: Int?,
        uid: String?
    ) {
        this.imageUrl = imageUrl
        this.fullName = fullName
        this.yearOfBirth = yearOfBirth
        this.yearOfBeath = yearOfBeath
        this.type = type
        this.fullText = fullText
        this.save = save
        this.uid = uid
    }


}