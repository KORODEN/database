package com.koroden.app5

import android.os.Parcel
import android.os.Parcelable

//Продукт
class Item() : Parcelable{

    //ID
    var id : Int = 0

    //Тип
    var kind : String = ""

    //Название
    var title : String = ""

    //Стоимость
    var price : Double = 0.0

    //Вес
    var weight : Double = 0.0

    //Фото
    var photo: String = ""

    //Сводная информация
    val info : String
        get() = "$kind $title ($price ₽) ($weight кг)"

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        kind = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        price = parcel.readDouble()
        weight = parcel.readDouble()
        photo = parcel.readString() ?: ""
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest !== null){
            dest.writeInt(id)
            dest.writeString(kind)
            dest.writeString(title)
            dest.writeDouble(price)
            dest.writeDouble(weight)
            dest.writeString(photo)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}