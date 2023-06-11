package com.example.androidlabs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private var elements: ArrayList<TODO>? = null
    private var myAdapter: MyListAdapter? = null
    private var todo: TODO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        val swUrgent = findViewById<Switch>(R.id.switch2)
        val addButton = findViewById<Button>(R.id.myButton)

        elements = ArrayList<TODO>()

        addButton.setOnClickListener { click: View? ->
            val listItem = editText.text.toString()
            todo = TODO()
            todo!!.setTodoText(listItem)
            todo!!.setUrgent(swUrgent.isChecked)
            elements!!.add(todo!!)
            myAdapter!!.notifyDataSetChanged()
            editText.setText("")
            swUrgent.isChecked = false
        }

        val myList = findViewById<ListView>(R.id.myList)
        myList.adapter = MyListAdapter().also { myAdapter = it }

        myList.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>?, view: View?, pos: Int, id: Long ->
                // Handle item click here
            }

        myList.onItemLongClickListener =
            OnItemLongClickListener { p: AdapterView<*>?, b: View?, pos: Int, id: Long ->
                val newView = layoutInflater.inflate(R.layout.todo, null)
                val tView = newView.findViewById<TextView>(R.id.textGoesHere)
                val alertDialogBuilder =
                    AlertDialog.Builder(this)

                // tView.setText(elements.get(pos).getTodoText());
                alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage(
                        "The selected row is: $pos\n${elements!![pos].getTodoText()}"
                    )
                    .setPositiveButton("Yes") { click: DialogInterface?, arg: Int ->
                        elements!!.remove(elements!![pos])
                        myAdapter!!.notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { click: DialogInterface?, arg: Int -> }
                    .setView(newView)
                    .create().show()
                true
            }
    }

    private inner class MyListAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return elements!!.size
        }

        override fun getItem(position: Int): TODO {
            return elements!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = layoutInflater
            val newView = inflater.inflate(R.layout.todo, parent, false)
            val tView = newView.findViewById<TextView>(R.id.textGoesHere)
            val todo: TODO = getItem(position)
            tView.text = todo.getTodoText()
            if (todo.isUrgent()) {
                tView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            } else {
                tView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
            }
            return newView
        }
    }
}

class TODO {
    private var todoText: String? = null
    private var urgent: Boolean = false

    fun setTodoText(listItem: String) {
        todoText = listItem
    }

    fun getTodoText(): String? {
        return todoText
    }

    fun setUrgent(checked: Boolean) {
        urgent = checked
    }

    fun isUrgent(): Boolean {
        return urgent
    }
}