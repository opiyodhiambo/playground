import jdk.jfr.ContentType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class SampleAPiRequest{

    init {
        processRequest()
    }

    @Autowired
    private lateinit var webClient: RestClient

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun processRequest() {

        val initiatePushRequest = InitiatePushRequest(
            BusinessShortCode = 174379,
            Password = "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjQxMDEyMTY1MzEz",
            Timestamp = "20241012165313",
            TransactionType = "CustomerPayBillOnline",
            Amount = 1,
            PartyA = 254708374149,
            PartyB = 174379,
            PhoneNumber = 254757346680,
            CallBackURL = "https://mydomain.com/path",
            AccountReference = "CompanyXLTD",
            TransactionDesc = "Payment of X"
        )

        val request = webClient.post()
            .uri("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
            .header("Authorization", "Bearer Wj3E0IVgCdJNicELj7bN3AOGxZs3")
            .contentType(MediaType.APPLICATION_JSON)
            .body(initiatePushRequest)
            .retrieve()
            .toBodilessEntity()

        logger.info("The response is :: ${request.body}")


    }
}

data class InitiatePushRequest(
    val BusinessShortCode: Long,
    val Password: String,
    val Timestamp: String,
    val TransactionType: String,
    val Amount: Int,
    val PartyA: Long,
    val PartyB: Long,
    val PhoneNumber: Long,
    val CallBackURL: String,
    val AccountReference: String,
    val TransactionDesc: String
)