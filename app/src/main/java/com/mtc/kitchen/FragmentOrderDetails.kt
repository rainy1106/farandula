package com.mtc.kitchen

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.mtc.BR
import com.mtc.R
import com.mtc.api.APIConstant
import com.mtc.api.APIConstant.Companion.ACTION_USB_PERMISSION
import com.mtc.databinding.FragmentOrderDetailsBinding
import com.mtc.demoppdf.Common
import com.mtc.demoppdf.PdfDocumentAdapter
import com.mtc.general.BaseFragment
import com.mtc.general.SharedPreference
import com.mtc.general.initViewModel
import com.mtc.interfaces.EventHandler
import com.mtc.print.starprint.starprntsdk.Communication.CommunicationResult
import com.mtc.print.starprint.starprntsdk.MainActivity
import com.mtc.print.usb.DeviceConnection
import com.mtc.print.usb.UsbConnection
import com.mtc.print.usb.UsbPrintersConnections
import com.mtc.print.usb.asyn.AsyncEscPosPrint
import com.mtc.print.usb.asyn.AsyncEscPosPrinter
import com.mtc.print.usb.asyn.AsyncUsbEscPosPrint
import com.mtc.print.usb.textparser.PrinterTextParserImg
import com.mtc.utils.NetworkUtility
import com.mtc.utils.OrderType
import com.mtc.utils.PDFClass
import com.mtc.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_order_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


open class FragmentOrderDetails() :
    BaseFragment<FragmentOrderDetailsBinding, OrderDetailsViewModel>(),
    View.OnClickListener {
    //    private var connectionMode: ConnectionModel? = null
//    lateinit var arrayList: OrderListItem.Result

    companion object {
        fun newInstance() = FragmentOrderDetails()
        lateinit var printArray: OrderListItem.Result
        lateinit var arrayList: OrderListItem.Result
    }

    override fun onStart() {
        super.onStart()
//        ConnectionLiveData(requireContext()).observe {
//            connectionMode = it
//            restart(it)
//        }
        setupRecyclerView()
    }

//    private fun restart(connection: ConnectionModel) {
//        connection.also {
//            mViewModel.setNetworkState(it)
//            if (!connection.isConnected) {
//                mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//            } else {
//                setupRecyclerView()
//            }
//        }
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        initObserver()

        setupRecyclerView()

        mDataBinding.chatwithcustomer.setOnClickListener(this)
//        mDataBinding.acceptOrder.setOnClickListener(this)
//        mDataBinding.orderReady.setOnClickListener(this)
        mDataBinding.printReceipt.setOnClickListener(this)
        mDataBinding.closeOrder.setOnClickListener(this)
    }

    private fun initObserver() {


        mViewModel.updateNewOrderCount("1", "1", "1", "1")

        mViewModel.newOrderCount.observe {
            newOrdersBDetail.badgeValue = it.toInt()
        }
        mViewModel.upComingCount.observe {
            upComingOrdersBDetail.badgeValue = it.toInt()
        }
    }

    private fun loadData() {
        mDataBinding.tablename.text = getList().table_name
        mDataBinding.subtablename.text = getList().seat_name
        mDataBinding.date.text = getList().getTDate()
        mDataBinding.dateTime.text = getList().getTime()

        if (getList().instractions.trim().isEmpty())
            mDataBinding.note.text =
                getString(R.string.note).plus(" " + getString(R.string.no_note_available))
        else
            mDataBinding.note.text =
                getString(R.string.note).plus(" " + getList().extra_items)


//        if (getList().getOrderStatus() == OrderType.ACCEPTED.name) {
//            mDataBinding.acceptOrder.text = getString(R.string.order_accepted)
//            mDataBinding.acceptOrder.visibility = View.GONE
//            mDataBinding.orderReady.visibility = View.VISIBLE
//        }


//        if (getList().getOrderStatus() == OrderType.READY.name) {
//            mDataBinding.acceptOrder.text = getString(R.string.order_accepted)
//            mDataBinding.orderReady.text = getString(R.string.served)
//            mDataBinding.chatwithcustomer.visibility = View.GONE
//            mDataBinding.acceptOrder.visibility = View.GONE
//            mDataBinding.printReceipt.visibility = View.VISIBLE
//            mDataBinding.closeOrder.visibility = View.VISIBLE
//        }

        if (getList().getOrderStatus() == OrderType.ONLYPAID.type) {
//            mDataBinding.acceptOrder.text = getString(R.string.order_accepted)
//            mDataBinding.orderReady.text = getString(R.string.served)
            mDataBinding.chatwithcustomer.visibility = View.GONE
//            mDataBinding.acceptOrder.visibility = View.GONE
//            mDataBinding.orderReady.visibility = View.GONE
            mDataBinding.printReceipt.visibility = View.VISIBLE
            mDataBinding.closeOrder.visibility = View.VISIBLE
        }

        val roundoff =
            mViewModel.getTotalCost(getList())//(mViewModel.getTotalCost(getList()) * 100.0).roundToInt() / 100.0
        "$ $roundoff".also { totalCostDetails.text = it }
        val roundoff2 = roundOffDecimal(mViewModel.getTotalCost(getList())).toString()
        "$ $roundoff2".also { totalCostDetailsTax.text = it }
    }

    private fun roundOffDecimal(number: Double): Double {
        val withTax = (number * SharedPreference.getKitchenTax(mDataBinding.root.context)) / 100
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble().plus(withTax)
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.chatwithcustomer -> {
                val fragmentMessages = FragmentMessages.newInstance()
                fragmentMessages.apply {
                    arguments = Bundle().apply {
                        putString("selected_seat_id", getList().seat_id)
                        putString("selected_table_id", getList().table_id)
                        putString(
                            "selected_table_name",
                            getList().table_name + "\t" + getList().seat_name
                        )
                    }
                }
                replaceFragment(fragmentMessages)
            }
//            R.id.acceptOrder -> {
//                if (acceptOrder.text.equals("Order Accepted")) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Order is already Accepted",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                } else {
//                    acceptOrder.text = "Order Accepted"
//                    val progressDialog = ProgressDialog(context)
//                    progressDialog.setCancelable(true)
//                    progressDialog.setMessage("Informing user , Please wait...")
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//                    progressDialog.show()
//                    mViewModel.updateOrderAccept(requireContext(), getList().order_id,
//                        object : EventHandler {
//                            override fun onComplete() {
//                                super.onComplete()
//                                progressDialog.dismiss()
//                            }
//
//                            override fun onSuccess() {
//                                super.onSuccess()
//                                replaceFragment(FragmentOrderListKitchen.newInstance())
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Order Accepted",
//                                    Toast.LENGTH_SHORT
//                                ).show()
////                                acceptOrder.text = "Order Accepted"
//                            }
//
//                            override fun onFailure(toString: String) {
//                                super.onFailure(toString)
//                                acceptOrder.text = requireContext().getString(R.string.accept_order)
//                                Toast.makeText(requireContext(), toString, Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        })
//                }
//
//
//            }
//            R.id.orderReady -> {
//
//                if (orderReady.text.equals("Served")) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Order is served",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                } else {
//                    orderReady.text = "Served"
//                    val progressDialog = ProgressDialog(context)
//                    progressDialog.setCancelable(true)
//                    progressDialog.setMessage("Informing user , Please wait...")
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//                    progressDialog.show()
//                    mViewModel.updateOrderIsReady(
//                        requireContext(),
//                        getList().order_id,
//                        object : EventHandler {
//                            override fun onSuccess() {
//                                super.onSuccess()
//                                replaceFragment(FragmentOrderListKitchen.newInstance())
//                            }
//
//                            override fun onFailure(toString: String) {
//                                super.onFailure(toString)
//                                orderReady.text = getString(R.string.order_is_ready)
//                                Toast.makeText(requireContext(), toString, Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//
//                            override fun onComplete() {
//                                super.onComplete()
//                                progressDialog.dismiss()
//                            }
//                        })
//                }
//
//
//            }
            R.id.printReceipt -> {

                var intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                CommunicationResult.count = 0;

                // printUsb()
//                if (PDFClass(requireContext()).checkPermissions()) {
//                    createPdf(Common.getPath(requireActivity()) + "test_pdf.pdf")
//                } else {
//                    requestPermission()
//                }
            }
            R.id.closeOrder -> {


                val progressDialog = ProgressDialog(context)
                progressDialog.setCancelable(false)
                progressDialog.setMessage("Closing , Please wait...")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.show()



                try {
                    FirebaseDatabase.getInstance().reference.child(APIConstant.CHATS_ROOM_NEW)
                        .child(getList().table_id)
                        .ref.removeValue { error, ref -> Log.v("Ref", ref.toString()) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


                val urlLine = APIConstant.ApiBaseUrl +
                        APIConstant.UPDATE_ORDER +
                        "order_id=${getList().order_id}" +
                        "&status=CLOSE"
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val rss = NetworkUtility.APIrequest(urlLine)
                        withContext(Dispatchers.Main) {
                            replaceFragment(FragmentOrderListKitchen.newInstance())
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    } finally {
                        progressDialog.dismiss()
                    }

                }
            }

        }
    }

    // on below line we are creating a function to request permission.
    fun requestPermission() {
        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PDFClass.PERMISSION_CODE
        )
    }

//    private fun createPdf(path: String) {
//
//
//        val progressDialog = ProgressDialog(requireContext())
//        progressDialog.setTitle("Printing Bill, Please wait")
//        progressDialog.setMessage("Processing")
//        progressDialog.show()
//
//
////        if (File(path).exists())
////            File(path).delete()
//        if (!File(path).exists()) {
//            File(path).createNewFile()
//        }
//
//        try {
//            val document = Document()
//            //save
//            val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(path))
//            //open
//            document.open()
//            //setting
////            Rectangle pagesize = new Rectangle(288, 720);
////            Document document = new Document(pagesize);
////            In this case, you'll have pages that measure 4 by 10 inch:
////
////            288 user units = 288 pt = 4 x 72pt = 4 inch
////            720 user units = 720 pt = 10 x 72pt = 10 inch
//            document.pageSize = PageSize.A4
//            //
//            document.addCreationDate()
//            document.addAuthor("Test")
//            document.addCreator("Test C")
//            //font
//            val baseColor = BaseColor(0, 0, 0, 0)
//            val fontSize = 20f
//            val valueFontSize = 20f
//
//            //custome_font
//            val fontName =
//                BaseFont.createFont("assets/font/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED)
//
//            // Creating an ImageData object
//            val bm = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//            val stream = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//            val myImg = Image.getInstance(stream.toByteArray())
//            myImg.alignment = Element.ALIGN_CENTER
//            document.add(myImg)
//
//
//            addLineSpace(document)
//            addLineSpace(document)
//
//            //title
//            val titleFont = Font(fontName, 22f, Font.NORMAL, BaseColor.BLACK)
//            val orderNumberValueTitle = Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK)
//
//            addNewItem(document, "Farandula Pizza & Restaurant", Element.ALIGN_CENTER, titleFont)
//            addLineSpace(document)
//            addLineSpace(document)
//            addLineSpace(document)
//            //address
//            val orderNumberAdd = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItemLeftANdRight(
//                document, "\t1200 Sw 67 Ave", getCDate(), titleFont,
//                orderNumberAdd
//            )
//
//            //address
//            val orderNumberAdd1 = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItemLeftANdRight(
//                document, "\tMiami, FL", getCTime(), titleFont,
//                orderNumberAdd1
//            )
//
//            //address
//            val orderNumberAdd2 = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItemLeftANdRight(
//                document, "\t33144", "Merlyn", titleFont,
//                orderNumberAdd2
//            )
//
//            //address
//            val orderNumberAdd3 = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItemLeftANdRight(
//                document, "\t(305)639-8492", "", titleFont,
//                orderNumberAdd3
//            )
//
//            //web
//            val orderNumberWeb = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItemLeftANdRight(
//                document, "\thttps://miami.lafaradulapizzeria.com", "", titleFont,
//                orderNumberWeb
//            )
//
//            addLineSpace(document)
//            addNewItemLeftANdRight(document, "Receipt : rTtv", "", titleFont, orderNumberValueTitle)
//            addNewItemLeftANdRight(
//                document, "Ticket : Ya se hizo 4", "",
//                titleFont,
//                orderNumberValueTitle
//            )
//            addNewItemLeftANdRight(
//                document,
//                "Authorization : 02136R",
//                "",
//                titleFont,
//                orderNumberValueTitle
//            )
//            addLineSpace(document)
//            addLineSpace(document)
//            addLineSpace(document)
//            val orderNumberTitle = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItem(document, "FOR HERE", Element.ALIGN_CENTER, orderNumberTitle)
//            addLineSpace(document)
//            addLineSpace(document)
//            //Add item
//
//            addNewItem(document, "Order Number :", Element.ALIGN_LEFT, orderNumberTitle)
//            addNewItem(document, getList().order_id, Element.ALIGN_LEFT, orderNumberValueTitle)
//            addLineSpace(document)
//
//            var total = 0.0
//            getList().cart.forEach {
//
//                //Item 1
//                total = total.plus(it.quantity.toInt().times(it.price.toDouble()))
//                addNewItemLeftANdRight(
//                    document,
//                    it.product_name,
//                    it.quantity + " X " + it.price,
//                    orderNumberTitle,
//                    orderNumberTitle
//                )
////                addNewItemLeftANdRight(document,it.quantity +" X "+it.price,it.quantity.toInt().times(it.price.toInt()).toString(), orderNumberTitle, orderNumberTitle)
//                addLineSpace(document)
//            }
//            val roundoff = (total * 100.0).roundToInt() / 100.0
//            addNewItemLeftANdRight(
//                document,
//                "Sub total",
//                "$ " + roundoff.toString(),
//                titleFont,
//                orderNumberValueTitle
//            )
//            addLineSpace(document)
//            addLineSpace(document)
//            addNewItemLeftANdRight(
//                document,
//                "Sales tax",
//                "$ 0.0 ",
//                titleFont,
//                orderNumberValueTitle
//            )
//            addLineSpace(document)
//            addNewItemLeftANdRight(document, "Tip", "$ 0.0 ", titleFont, orderNumberValueTitle)
//            addLineSpace(document)
//            addNewItemLeftANdRight(
//                document,
//                "Total",
//                "$ " + roundoff.toString(),
//                titleFont,
//                orderNumberValueTitle
//            )
//            addLineSpace(document)
//            addNewItemLeftANdRight(
//                document,
//                "Discover 3356 (Chip)",
//                "$ " + roundoff.toString(),
//                titleFont,
//                orderNumberValueTitle
//            )
//
//
//            addLineSpace(document)
//            addLineSpace(document)
//
//            addNewItem(
//                document,
//                "La Pizzeria de la farandula",
//                Element.ALIGN_CENTER,
//                orderNumberTitle
//            )
//            addLineSpace(document)
//            addLineSpace(document)
//
//            addNewItem(document, "No returns", Element.ALIGN_CENTER, orderNumberTitle)
//            addLineSpace(document)
//            document.close()
//
//            Toast.makeText(requireActivity(), "Document generated success", Toast.LENGTH_SHORT)
//                .show()
//
//            printPdf()
//        } catch (ex: java.lang.Exception) {
//            ex.printStackTrace()
//        } finally {
//            progressDialog.dismiss()
//        }
//    }

    private fun getCDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        return sdf.format(Date())
    }

    private fun getCTime(): String {
        val sdf = SimpleDateFormat("hh:mm a")
        return sdf.format(Date())
    }

    private fun addLineSpace(document: Document) {
        val paragraph = Paragraph("")
        paragraph.add(Chunk(VerticalPositionMark()))
        document.add(paragraph)
    }
//
//    private fun addLineSeperator(document: Document) {
//        val lineSeparator = LineSeparator()
//        lineSeparator.lineColor = BaseColor.BLACK
//        addLineSpace(document)
//        val chunk = Chunk()
//        document.add(chunk)
//        addLineSpace(document)
//
//    }

    private fun addNewItem(document: Document, text: String, alignCenter: Int, titleFont: Font) {
        val chunk = Chunk(text, titleFont)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignCenter
        document.add(paragraph)
    }

    private fun addNewItemLeftANdRight(
        document: Document,
        textLeft: String,
        textRight: String,
        textLeftFont: Font,
        textRightFont: Font
    ) {
        val chunkTextLeft = Chunk(textLeft, textLeftFont)
        val chunkTextRight = Chunk(textRight, textRightFont)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

//    private fun printPdf() {
//        val printManager = requireActivity().getSystemService(Context.PRINT_SERVICE) as PrintManager
//        try {
//            val pdfDocumnetAdapter = PdfDocumentAdapter(
//                requireActivity(), Common.getPath(requireContext()) + "test_pdf.pdf"
//            )
//            printManager.print("Document", pdfDocumnetAdapter, PrintAttributes.Builder().build())
//        } catch (ex: java.lang.Exception) {
//
//        }
//    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_order_details

    override fun getViewModel(): OrderDetailsViewModel {
        return initViewModel {
            OrderDetailsViewModel()
        }
    }

    private fun setupRecyclerView() {

        val mList = getList()

//        if (mViewModel.isInternetConnected(requireContext())) {
        if (mList.cart.size > 0) {
            val adapter = OrderDetailsAdapter(mList.cart)
            mDataBinding.recyclerViewOrder.setHasFixedSize(true)
            mDataBinding.recyclerViewOrder.visibility = View.VISIBLE
            mDataBinding.noTextView.visibility = View.GONE
            mDataBinding.recyclerViewOrder.adapter = adapter
            //adapter.onDashboardClickListener = mViewModel
            val layoutManager = LinearLayoutManager(requireContext())
            mDataBinding.recyclerViewOrder.layoutManager = layoutManager
            val lineDivider =
                ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)
            mDataBinding.recyclerViewOrder.addItemDecoration(
                SimpleDividerItemDecoration(
                    requireContext(),
                    lineDivider!!
                )
            )
        } else {
            mDataBinding.recyclerViewOrder.visibility = View.GONE
            mDataBinding.noTextView.visibility = View.VISIBLE
        }

//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }


//        if (mViewModel.isInternetConnected(requireContext())) {
//            showLoader(getString(R.string.loading))
//
//
//            mViewModel.getOrderDetails(requireContext(),
//                object : onResponseAPI {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onSuccess(mList: ArrayList<com.mtc.order.OrderItem>) {
//
//
//                    }
//
//                    override fun onSuccessKitchen(mList: ArrayList<com.mtc.kitchen.OrderItem>) {
//
//                        hideLoader()
//                    }
//
//                    override fun onSuccessKitchenOrderListItem(arrayList: ArrayList<OrderListItem.Result>) {
//                        TODO("Not yet implemented")
//                    }
//
//
//                    override fun onFailure() {
//                        hideLoader()
//                        mDataBinding.recyclerViewOrder.visibility = View.GONE
//                        mDataBinding.noTextView.visibility = View.VISIBLE
//                    }
//
//                })
//
//        } else {
//            mViewModel.showToast(getString(R.string.no_internet_connection), requireContext())
//        }


    }

    fun setList(_arrayList: OrderListItem.Result) {
        arrayList = _arrayList
        printArray = arrayList
    }

    private fun getList(): OrderListItem.Result {
        return arrayList
    }


    /*------------------------------*/
//    open fun printUsb() {
//        val usbConnection: UsbConnection? =
//            UsbPrintersConnections.selectFirstConnected(requireContext())
//        val usbManager = requireActivity().getSystemService(Context.USB_SERVICE) as UsbManager?
//        if (usbConnection == null || usbManager == null) {
//            AlertDialog.Builder(requireContext())
//                .setTitle("USB Connection")
//                .setMessage("No USB printer found.")
//                .show()
//            return
//        }
//        val permissionIntent = PendingIntent.getBroadcast(
//            requireContext(),
//            0,
//            Intent(ACTION_USB_PERMISSION),
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
//        )
//        val filter = IntentFilter(ACTION_USB_PERMISSION)
//        requireActivity().registerReceiver(usbReceiver, filter)
//        usbManager.requestPermission(usbConnection.device, permissionIntent)
////*************************************************************//
////        val usbConnection = UsbPrintersConnections.selectFirstConnected(requireActivity())
////        val usbManager = requireActivity().getSystemService(Context.USB_SERVICE) as UsbManager?
////
////        if (usbConnection == null || usbManager == null) {
////            AlertDialog.Builder(requireActivity())
////                .setTitle("USB Connection")
////                .setMessage("No USB printer found.")
////                .show()
////            return
////        }
////
////        val permissionIntent =
////            PendingIntent.getBroadcast(requireContext(), 0, Intent(ACTION_USB_PERMISSION), 0)
////        val filter = IntentFilter(ACTION_USB_PERMISSION)
////        requireActivity().registerReceiver(usbReceiver, filter)
////        usbManager.requestPermission(usbConnection.device, permissionIntent)
//    }

//    private val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized(this) {
//                    val usbManager =
//                        requireActivity().getSystemService(Context.USB_SERVICE) as UsbManager?
//                    val usbDevice =
//                        intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        if (usbManager != null && usbDevice != null) {
//                            printIt(usbManager, usbDevice)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    open fun printIt(usbManager: UsbManager?, usbDevice: UsbDevice?) {
//        try {
//            val printer = EscPosPrinter(
//                UsbConnection(usbManager, usbDevice),
//                203, 65f, 42
//            )
//            printer.printFormattedTextAndCut(
//                """
//                [C]<img>${
//                    PrinterTextParserImg.bitmapToHexadecimalString(
//                        printer,
//                        requireActivity().resources
//                            .getDrawableForDensity(
//                                R.mipmap
//                                    .ic_launcher, DisplayMetrics.DENSITY_MEDIUM
//                            )
//                    )
//                }</img>
//                [L]
//                [C]<u><font size='big'>ORDER N°1125</font></u>
//                [L]
//                [L] _________________________________________
//                [L] Description [R]Amount
//                [L]
//                [L] <b>Beef Burger [R]10.00
//                [L] Sprite-200ml [R]3.00
//                [L] _________________________________________
//                [L] TOTAL [R]13.00 BD
//                [L] Total Vat Collected [R]1.00 BD
//                [L]
//                [L] _________________________________________
//                [L]
//                [C]<font size='tall'>Customer Info</font>
//                [L] Megha Tavrech
//                [L] BH,Mp
//                [L] INDIA
//                [L] Tel : +910000000000
//                """.trimIndent()
//            )
//            printer.disconnectPrinter()
//        } catch (e: EscPosConnectionException) {
//            e.printStackTrace()
//            AlertDialog.Builder(activity)
//                .setTitle("Broken connection")
//                .setMessage(e.message)
//                .show()
//        } catch (e: EscPosParserException) {
//            e.printStackTrace()
//            AlertDialog.Builder(activity)
//                .setTitle("Invalid formatted text")
//                .setMessage(e.message)
//                .show()
//        } catch (e: EscPosEncodingException) {
//            e.printStackTrace()
//            AlertDialog.Builder(activity)
//                .setTitle("Bad selected encoding")
//                .setMessage(e.message)
//                .show()
//        } catch (e: EscPosBarcodeException) {
//            e.printStackTrace()
//            AlertDialog.Builder(activity)
//                .setTitle("Invalid barcode")
//                .setMessage(e.message)
//                .show()
//        }
//    }


    private val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val usbManager =
                        requireActivity().getSystemService(Context.USB_SERVICE) as UsbManager?
                    val usbDevice =
                        intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            printViaAsync(usbManager, usbDevice)
                        }
                    }
                }
            }
        }
    }

    private fun printViaAsync(usbManager: UsbManager, usbDevice: UsbDevice) {
        AsyncUsbEscPosPrint(context, object : AsyncEscPosPrint.OnPrintFinished() {
            override fun onError(asyncEscPosPrinter: AsyncEscPosPrinter?, codeException: Int) {
                Log.e(
                    "Async.OnPrintFinished",
                    "AsyncEscPosPrint.OnPrintFinished : An error occurred !"
                )
            }

            override fun onSuccess(asyncEscPosPrinter: AsyncEscPosPrinter?) {
                Log.i(
                    "Async.OnPrintFinished",
                    "AsyncEscPosPrint.OnPrintFinished : Print is finished !"
                )
            }
        }
        ).execute(getAsyncEscPosPrinter(getList(), UsbConnection(usbManager, usbDevice)))
    }

    /**
     * Asynchronous printing
     * + format.format(Date()) + "
     */
    fun getAsyncEscPosPrinter(orders: OrderListItem.Result, printerConnection: DeviceConnection):
            AsyncEscPosPrinter {

        var total = 0.0
        getList().cart.forEach {
            total = total.plus(it.quantity.toInt().times(it.price.toDouble()))
        }
        val roundoff = (total * 100.0).roundToInt() / 100.0
        val tax = (roundoff * 7).roundToInt() / 100.0

        val printer = AsyncEscPosPrinter(printerConnection, 203, 55f, 32)
        return printer.addTextToPrint(
            "[C]<img>${
                PrinterTextParserImg.bitmapToHexadecimalString(
                    printer,
                    requireActivity().applicationContext.resources.getDrawableForDensity(
                        R.mipmap.ic_launcher,
                        DisplayMetrics.DENSITY_DEVICE_STABLE
                    )
                )
            }</img>\n" +
                    "[C]${orders.sub_total}\n" +
                    "[C]${orders.cart[0].product_name + " " + orders.cart[0].quantity + " [R]$ " + orders.cart[0].price}\n" +
//                    orders.cart.forEach {
//                        "[L]" + it.product_name + " " + it.quantity + " [R]$" + it.price.toDouble()
//                            .times(it.quantity.toInt()) + "\n"
//                        "[L]\t" + it.extra_items + "\n"
//                    } +
                    "[C]------------------------------\n" +
                    "[L]Purchase Subtotal : [R]$ ${roundoff}\n" +
                    "[L]Sales Tax(7%) : [R]$ $tax \n" +
                    "[C]------------------------------\n" +
                    "[L]Total :    [R]$ ${roundoff.plus(tax)} \n" +
                    "[C]------------------------------\n" +
                    "[R] ${getCDate()} at ${getCTime()} \n" +
                    "[C]Farandula Pizza & Restaurant\n" +
                    "[C]2846 Palm Ave\n" +
                    "[C]Hialeah, FL33010\n" +
                    "[C]786-332-3779\n" +
                    "[C]------------------------------\n" +
                    "[L]Return Policy : No Return\n" +
                    "[C]------------------------------\n"
        )
    }
}