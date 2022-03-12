package cz.speedy.clankipediabot.util

import com.moandjiezana.toml.Toml
import java.io.File
import java.io.InputStream
import java.nio.file.Files

class ConfigUtil {

    companion object {

        fun loadConfig(fileName: String): Toml {
            val file = File(fileName)
            if (!file.exists()) {
                val inputStream: InputStream? = ConfigUtil::class.java.getResourceAsStream("/$fileName")
                if (inputStream != null) {
                    Files.copy(inputStream, file.toPath())
                    inputStream.close()
                } else {
                    file.createNewFile()
                }
            }
            return Toml().read(file)
        }
    }

}