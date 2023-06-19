package com.example.androidlabs


//import list
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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var elements: ArrayList<TODO>? = null
    private lateinit var myAdapter: MyListAdapter
    private var todo: TODO? = null
    private var databaseHelper: TodoDatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        val swUrgent = findViewById<SwitchCompat>(R.id.switch2)
        val addButton = findViewById<Button>(R.id.myButton)

        elements = ArrayList()
        myAdapter = MyListAdapter()

        addButton.setOnClickListener {_: View? ->
            val listItem = editText.text.toString()
            todo = TODO()
            todo!!.setTodoText(listItem)
            todo!!.setUrgent(swUrgent.isChecked)
            elements!!.add(todo!!)
            myAdapter.notifyDataSetChanged()
            editText.setText("")
            swUrgent.isChecked = false
        }

        val myList = findViewById<ListView>(R.id.myList)
        myList.adapter = myAdapter

        myList.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, _: Int, _: Long ->
            // Handle item click here
        }

        myList.onItemLongClickListener = OnItemLongClickListener { _: AdapterView<*>?, _: View?, pos: Int, _: Long ->
            val newView = layoutInflater.inflate(R.layout.todo, null)
            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder.setTitle("Do you want to delete this?")
                .setMessage("The selected row is: $pos\n${elements!![pos].getTodoText()}")
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                    elements!!.remove(elements!![pos])
                    myAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("No") { _: DialogInterface?, _: Int -> }
                .setView(newView)
                .create().show()
            true
        }


        databaseHelper = TodoDatabaseHelper(this)

        val allTodoItems = databaseHelper!!.allTodoItems

        printCursor(allTodoItems)
    }

    private fun printCursor(allTodoItems: List<TodoItem>) {
        val versionNumber = databaseHelper?.readableDatabase?.version ?: -1
        val columnCount = if (allTodoItems.isNotEmpty()) allTodoItems[0].javaClass.declaredFields.size else 0

        println("Database Version: $versionNumber")


        println("Number of Columns: $columnCount")


        val columnNames = if (allTodoItems.isNotEmpty()) allTodoItems[0].javaClass.declaredFields.joinToString(", ") { it.name } else ""
        println("Column Names: $columnNames")

        val resultCount = allTodoItems.size
        println("Number of Results: $resultCount")

        if (resultCount > 0) {
            for (todoItem in allTodoItems) {
                val rowValues = "${todoItem.todoText}, ${todoItem.isUrgent}"
                println("Row: $rowValues")
            }
        } else {
            println("No results found.")
        }
    }


    private inner class MyListAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return elements?.size ?: 0
        }

        override fun getItem(position: Int): TODO? {
            return elements?.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            val holder: ViewHolder

            if (itemView == null) {
                itemView = layoutInflater.inflate(R.layout.todo, parent, false)
                holder = ViewHolder(itemView)
                itemView.tag = holder
            } else {
                holder = itemView.tag as ViewHolder
            }

            val todo: TODO = getItem(position) as TODO
            holder.tView.text = todo.getTodoText()

            if (todo.isUrgent()) {
                holder.tView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.red))
                holder.tView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            } else {
                holder.tView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.transparent))
                holder.tView.setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
            }

            return itemView!!
        }


        private inner class ViewHolder(view: View) {
            val tView: TextView = view.findViewById(R.id.textGoesHere)
        }
    }

}

class TODO {
    private var urgent: Boolean = false
    private var todoText: String? = null

    fun setTodoText(todoText: String?) {
        this.todoText = todoText
    }

    fun getTodoText(): String? {
        return todoText
    }

    fun setUrgent(urgent: Boolean) {
        this.urgent = urgent
    }

    fun isUrgent(): Boolean {
        return urgent
    }
}