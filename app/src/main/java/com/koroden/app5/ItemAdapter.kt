package com.koroden.app5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ItemAdapter(context: Context, items: ArrayList<Item>) : BaseAdapter() {

    var ctx: Context = context
    var objects: ArrayList<Item> = items
    var inflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Формирование разметки, содержащей строку данных
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // Если разметка ещё не существует, создаём её по шаблону
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.listview_layout_item, parent, false)

        // Получение объекта с информацией о продукте
        val s = objects[position]

        // Заполнение элементов данными из объекта
        var v = view?.findViewById(R.id.item_title) as TextView
        v.text = s.title
        v = view.findViewById(R.id.item_kind) as TextView
        v.text = s.kind
        return view
    }

    // Получение количества элементов в списке
    override fun getCount(): Int {
        return objects.size
    }

    // Получение элемента данных в указанной строке
    override fun getItem(position: Int): Any {
        return objects[position]
    }

    // Получение идентификатора элемента в указанной строке
    // Часто вместо него возвращается позиция элемента
    override fun getItemId(position: Int): Long {
        return position.toLong()

    }
}