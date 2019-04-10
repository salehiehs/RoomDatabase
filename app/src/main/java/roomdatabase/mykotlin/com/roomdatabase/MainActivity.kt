package roomdatabase.mykotlin.com.roomdatabase

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.text.style.TtsSpan
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import roomdatabase.mykotlin.com.roomdatabase.Database.UserRepository
import roomdatabase.mykotlin.com.roomdatabase.Local.UserDataSource
import roomdatabase.mykotlin.com.roomdatabase.Local.UserDatabase
import roomdatabase.mykotlin.com.roomdatabase.Model.User
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var adapter: ArrayAdapter<*>
    var userList:MutableList<User> =ArrayList()

    //Database
    private var compositeDisposable:CompositeDisposable?=null
    private var userRepository:UserRepository?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Init
        compositeDisposable = CompositeDisposable()

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,userList)
        registerForContextMenu(lst_users)
        lst_users!!.adapter = adapter

        //Database
        val userdatabase = UserDatabase.getInstance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userdatabase.userDAO()))

        //Load All Data From Database
        LoadData()
        //Button Add
        fab_add.setOnClickListener(){
            val user = User()
            user.name = "EmDev"
            user.email = UUID.randomUUID().toString()+"@gmail.com"
            inserUser(user)
        }


    }



    private fun LoadData() {
        var disposable = userRepository!!.allUsers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({users -> onGetAlluserSuccess(users) })
                {
                    throwable ->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                }

        compositeDisposable!!.add(disposable)
    }

    private fun onGetAlluserSuccess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return  super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId)
        {
            R.id.clear -> deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu.setHeaderTitle("Select action:")

        menu.add(Menu.NONE,0,Menu.NONE,"Update")
        menu.add(Menu.NONE,1,Menu.NONE,"Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val user = userList[info.position]


        when(item.itemId)
        {
           0 ->
            {
                val edtname = EditText(this@MainActivity)
                edtname.setText(user.name)
                edtname.hint = "Enter Your Name"

                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Edit")
                        .setMessage("Edit user name")
                        .setView(edtname)
                        .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener{ dialog, which->
                            if(TextUtils.isEmpty(edtname.text.toString()))
                                return@OnClickListener
                            else
                            {
                                user.name = edtname.text.toString()
                                updateUser(user)
                            }
                        }).setNegativeButton(android.R.string.cancel)
                        {
                            dialog, which -> dialog.dismiss()
                        }.create()
                        .show()
            }
            //Delete
            1->
            {     AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete")
                    .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener{
                        dialog, which ->
                        deleteUser(user)
                    }).create()
                    .show()
            }

        }

        return super.onContextItemSelected(item)
    }

    private fun deleteAllUsers() {

        try {
            val disposable = Observable.create(ObservableOnSubscribe<Any> {e->
                userRepository!!.deleteAllUsers()
                e.onComplete()
            })
                    .observeOn(Schedulers.io())
                    .subscribe(io.reactivex.functions.Consumer {

                    },
                            io.reactivex.functions.Consumer {

                                throwable-> Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                            },
                            Action { LoadData() }       )

            compositeDisposable!!.addAll(disposable)
        }
        catch (e:Exception)
        {}
    }

    private fun inserUser(user:User) {
        val disposable = Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.InsertUser(user)
            e.onComplete()
        })
                .observeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer {

                },
                        io.reactivex.functions.Consumer {
                            throwable-> Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { LoadData() } )

        compositeDisposable!!.addAll(disposable)
    }

    private fun deleteUser(user: User) {

        val disposable = Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.deleteUser(user)
            e.onComplete()
        })
                .observeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer {

                },
                        io.reactivex.functions.Consumer {

                            throwable-> Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { LoadData() }       )

        //compositeDisposable!!.add(userRepository!!.deleteUser(user).

       compositeDisposable!!.addAll(disposable)
    }

    private fun updateUser(user: User) {

        val disposable = Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.updateUser(user)
            e.onComplete()
        })
                .observeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer {

                },
                        io.reactivex.functions.Consumer {

                            throwable-> Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { LoadData() }       )

        compositeDisposable!!.addAll(disposable)
    }

    override fun onDestroy() {
        compositeDisposable!!.clear()
        super.onDestroy()
    }


}
