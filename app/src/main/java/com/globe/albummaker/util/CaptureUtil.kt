import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.SystemClock
import android.util.Log
import android.view.View

class CaptureUtil {


    companion object {

        private val CAPTURE_PATH = "/TestAlbum"


        fun captureView(context: Context, view: View, saltName: String): String {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)

            val capturView = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight, Bitmap.Config.ARGB_8888
            )
            val screenShotCanvas = Canvas(capturView)
            view.draw(screenShotCanvas)

            val fileName =
                "${SystemClock.currentThreadTimeMillis()}${SystemClock.currentThreadTimeMillis()}$saltName.png"


            Log.e("파일생성이름?",fileName)
            val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            capturView.compress(Bitmap.CompressFormat.PNG, 100, fos)

            fos.flush()
            fos.close()
            view.isDrawingCacheEnabled = false
            return fileName

        }


//        fun captureView(context: Context, view: View, saltName: String): String {
//            view.isDrawingCacheEnabled = true
//            view.buildDrawingCache(true)
//
//            val capturView = Bitmap.createBitmap(
//                view.measuredWidth,
//                view.measuredHeight, Bitmap.Config.ARGB_8888
//            )
//            val screenShotCanvas = Canvas(capturView)
//            view.draw(screenShotCanvas)
//
//
//            val fos: FileOutputStream
//            val folderPath = Environment.getExternalStorageDirectory().absolutePath + CAPTURE_PATH
//            val folder = File(folderPath)
//            if (!folder.exists())
//                folder.mkdirs()
//
//
//            val filePath =
//                "$folderPath/${SystemClock.currentThreadTimeMillis()}${SystemClock.currentThreadTimeMillis()}$saltName.png"
//
//            val fileCacheItem = File(filePath)
//            //TODO 예외처리, 이슈처리를 위해 예외처리 제거 상태
//            fos = FileOutputStream(fileCacheItem)
//
//            capturView.compress(Bitmap.CompressFormat.PNG, 100, fos)
//
//            fos.flush()
//            fos.close()
//            view.isDrawingCacheEnabled = false
//            return filePath
//
//        }

    }
}