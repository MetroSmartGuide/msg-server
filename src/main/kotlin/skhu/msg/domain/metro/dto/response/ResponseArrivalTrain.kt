package skhu.msg.domain.metro.dto.response

data class ResponseArrivalTrain(
    var subwayId: String? = null,
    var updnLine: String? = null,
    var trainLineNm: String? = null,
    var statnFid: String? = null,
    var statnTid: String? = null,
    var statnId: String? = null,
    var statnNm: String? = null,
    var btrainNo: String? = null,
    var bstatnId: String? = null,
    var bstatnNm: String? = null,
    var recptnDt: String? = null,
    var arvlMsg2: String? = null,
    var arvlMsg3: String? = null,
) {

    companion object {
        fun create(
            subwayId: String? = null,
            updnLine: String? = null,
            trainLineNm: String? = null,
            statnFid: String? = null,
            statnTid: String? = null,
            statnId: String? = null,
            statnNm: String? = null,
            btrainNo: String? = null,
            bstatnId: String? = null,
            bstatnNm: String? = null,
            recptnDt: String? = null,
            arvlMsg2: String? = null,
            arvlMsg3: String? = null,
        ) = ResponseArrivalTrain(
                subwayId = subwayId,
                updnLine = updnLine,
                trainLineNm = trainLineNm,
                statnFid = statnFid,
                statnTid = statnTid,
                statnId = statnId,
                statnNm = statnNm,
                btrainNo = btrainNo,
                bstatnId = bstatnId,
                bstatnNm = bstatnNm,
                recptnDt = recptnDt,
                arvlMsg2 = arvlMsg2,
                arvlMsg3 = arvlMsg3,
        )
    }

}