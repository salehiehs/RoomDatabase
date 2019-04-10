package roomdatabase.mykotlin.com.roomdatabase.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "users" )
class User
{
    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int=0


    @ColumnInfo(name = "name")
    var name:String? = null

    @ColumnInfo(name = "email")
    var email:String? = null

    constructor()

    override fun toString(): String {
        return StringBuilder(name)
                .append("\n")
                .append(email).toString()
    }


}