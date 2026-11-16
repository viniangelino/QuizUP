package br.com.quizup.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pergunta::class, Pontuacao::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun perguntaDao(): PerguntaDao
    abstract fun pontuacaoDao(): PontuacaoDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        /** Singleton: o app inteiro usa sempre o mesmo banco. */
        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_futebol.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
