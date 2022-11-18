//package ai.ftech.base.common
//
//import com.example.dcom.presentation.extension.getAppColor
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.ActivityInfo
//import android.content.pm.PackageManager
//import android.content.res.Resources
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.InflateException
//import android.view.View
//import android.view.ViewTreeObserver
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.LayoutRes
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import com.example.dcom.presentation.common.BaseFragment
//import com.example.dcom.presentation.common.BaseView
//
//abstract class BaseActivity(@LayoutRes protected val layoutId: Int) :
//    AppCompatActivity(), BaseView {
//
//    companion object {
//        const val FRAGMENT_NAME = "FRAGMENT_NAME"
//        const val FRAGMENT_BUNDLE = "FRAGMENT_BUNDLE"
//    }
//
//    protected val TAG = this::class.java.simpleName
//    private var permissionListener: PermissionListener? = null
//    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            it.forEach { (k, v) ->
//                if (v) {
//                    val lastKey = it.keys.last()
//                    if (lastKey == k) {
//                        permissionListener?.onAllow()
//                    }
//                } else {
//                    if (!shouldShowRequestPermissionRationale(k)) {
//                        permissionListener?.onNeverAskAgain()
//                        return@registerForActivityResult
//                    }
//                }
//                permissionListener?.onDenied()
//            }
//        }
//    }
//
//    private val safeActionManager = SafeActionManager()
//    private var keyboardGlobalListener: ViewTreeObserver.OnGlobalLayoutListener? = null
//
//    init {
//
//    }
//
//    //region lifecycle
//    override fun onCreate(savedInstanceState: Bundle?) {
//        onPrepareInitView()
//        if (isOnlyPortraitScreen()) {
//            setPortraitScreen()
//        }
//        super.onCreate(savedInstanceState)
//        try {
//            if (isFixSingleTask()) {
//                if (!isTaskRoot) {
//                    finish()
//                    return
//                }
//            }
//            attachView()
//            onInitBinding()
//            onInitView()
//            onObserverViewModel()
//        } catch (e: InflateException) {
//            e.printStackTrace()
//            Log.e(TAG, "${e.message}")
//        } catch (e: Resources.NotFoundException) {
//            e.printStackTrace()
//            Log.e(TAG, "${e.message}")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(TAG, "${e.message}")
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        safeActionManager.setSafeActionState(true)
//        safeActionManager.doWaitingAction()
//    }
//
//    override fun onPause() {
//        safeActionManager.setSafeActionState(false)
//        super.onPause()
//    }
//
//    //endregion
//
//    open fun isFixSingleTask(): Boolean = false
//
//    open fun isOnlyPortraitScreen(): Boolean = false
//
//
//    open fun isSoftInputAdjustResize(): Boolean = false
//
//
//    open fun attachView() {
//        setContentView(layoutId)
//    }
//
//    //region navigate screen
//    fun navigateTo(clazz: Class<out BaseActivity>, onCallback: (Intent) -> Unit = {}) {
//        val intent = Intent(this, clazz)
//        onCallback.invoke(intent)
//        startActivity(intent)
//    }
//
//    fun navigateTo(
//        clazz: Class<out BaseActivity>,
//        bundle: Bundle,
//        onCallback: (Intent) -> Unit = {}
//    ) {
//        val intent = Intent(this, clazz)
//        intent.putExtras(bundle)
//        onCallback.invoke(intent)
//        startActivity(intent)
//    }
//
//    fun navigateTo(
//        clazz: Class<out BaseActivity>,
//        fragmentClazz: Class<out BaseFragment>,
//        onCallback: (Intent) -> Unit = {}
//    ) {
//        val intent = Intent(this, clazz)
//        intent.putExtra(FRAGMENT_NAME, fragmentClazz.name)
//        onCallback.invoke(intent)
//        startActivity(intent)
//    }
//
//    fun navigateTo(
//        clazz: Class<out BaseActivity>,
//        fragmentClazz: Class<out BaseFragment>,
//        bundle: Bundle,
//        onCallback: (Intent) -> Unit = {}
//    ) {
//        val intent = Intent(this, clazz)
//        intent.putExtra(FRAGMENT_NAME, fragmentClazz.name)
//        intent.putExtra(FRAGMENT_BUNDLE, bundle)
//        onCallback.invoke(intent)
//        startActivity(intent)
//    }
//
//    fun navigateBack() {
//        onBackPressed()
//    }
//
//    fun replaceFragment(
//        fragment: BaseFragment,
//        bundle: Bundle? = null,
//        keepToBackStack: Boolean = true,
//        fragmentAnim: FragmentAnim = FragmentAnim()
//    ) {
//        includeFragment(
//            fragment,
//            bundle,
//            getContainerId(),
//            true,
//            keepToBackStack,
//            fragmentAnim
//        )
//    }
//
//    fun addFragment(
//        fragment: BaseFragment,
//        bundle: Bundle? = null,
//        keepToBackStack: Boolean = true,
//        fragmentAnim: FragmentAnim = FragmentAnim()
//    ) {
//        includeFragment(
//            fragment,
//            bundle,
//            getContainerId(),
//            false,
//            keepToBackStack,
//            fragmentAnim
//        )
//    }
//
//    fun backFragment() {
//        supportFragmentManager.popBackStack()
//    }
//
//    fun backFragment(tag: String) {
//        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//    }
//
//    fun clearStackFragment() {
//        supportFragmentManager.let { fm ->
//            fm.backStackEntryCount.let { count ->
//                for (i in 0..count) {
//                    fm.popBackStack()
//                }
//            }
//        }
//    }
//
//    fun getCurrentFragment(): Fragment? {
//        val fragmentList = supportFragmentManager.fragments
//        return fragmentList.lastOrNull()
//    }
//    //endregion
//
//    //region status bar
//    fun setStatusColor(color: Int = android.R.color.black, isDarkText: Boolean = true) {
//        window?.apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                decorView.let {
//                    ViewCompat.getWindowInsetsController(it)?.apply {
//                        // Light text == dark status bar
//                        // máy ảo bị bug có lúc hiển thị sai cái này, trên device thật vẫn sẽ show bt
//                        isAppearanceLightStatusBars = isDarkText
//                    }
//                }
//            } else {
//                //set text status old api
//                decorView.let {
//                    it.systemUiVisibility =
//                        if (!isDarkText) {
//                            it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//                        } else {
//                            it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                        }
//                }
//            }
//
//            //set status color
//            statusBarColor = getAppColor(color)
//        }
//    }
//
//
//    //endregion
//
//    //region safe action
//    fun doSafeAction(action: WAITING_ACTION) {
//        safeActionManager.doSafeAction(action)
//    }
//
//    fun resetWaitingAction() {
//        safeActionManager.resetWaitingActionList()
//    }
//    //endregion
//
//
//    //region request permision
//    fun doRequestPermission(
//        permissions: Array<String>,
//        listener: PermissionListener
//    ) {
//        this.permissionListener = listener
//        if (checkPermission(permissions)) {
//            this.permissionListener?.onAllow()
//        } else {
//            launcher.launch(permissions)
//        }
//    }
//
//    private fun checkPermission(permissions: Array<String>): Boolean {
//        permissions.forEach {
//            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
//                return false
//            }
//        }
//        return true
//    }
//    //endregion
//
//    //region orientation
//    @SuppressLint("SourceLockedOrientationActivity")
//    private fun setPortraitScreen() {
//        try {
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(TAG, "${e.message}")
//        }
//    }
//    //endregion
//
//    //region fragment backstack
//    private fun includeFragment(
//        fragment: Fragment,
//        bundle: Bundle?,
//        containerId: Int,
//        isReplace: Boolean,
//        keepToBackStack: Boolean,
//        fragmentAnim: FragmentAnim
//    ) {
//        if (getContainerId() == LAYOUT_INVALID) {
//            throw IllegalArgumentException("Cần phải gán container id để replace fragment")
//        }
//        try {
//            val tag = fragment::class.java.simpleName
//            bundle?.let {
//                fragment.arguments = it
//            }
//            supportFragmentManager.beginTransaction().apply {
//                /*setCustomAnimations(
//                    fragmentAnim.enter,
//                    fragmentAnim.exist,
//                    fragmentAnim.popEnter,
//                    fragmentAnim.popExit
//                )*/
//                if (isReplace) {
//                    replace(containerId, fragment, tag)
//                } else {
//                    add(containerId, fragment, tag)
//                }
//                if (keepToBackStack) {
//                    addToBackStack(tag)
//                }
//                commit()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//    //endregion
//
//    interface PermissionListener {
//        fun onAllow()
//        fun onDenied()
//        fun onNeverAskAgain()
//    }
//
//}
