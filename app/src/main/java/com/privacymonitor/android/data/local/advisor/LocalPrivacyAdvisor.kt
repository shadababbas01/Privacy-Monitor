package com.privacymonitor.android.data.local.advisor

import com.privacymonitor.android.domain.repository.AdvisorResponse
import com.privacymonitor.android.domain.repository.PrivacyAdvisor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPrivacyAdvisor @Inject constructor() : PrivacyAdvisor {

    override suspend fun explain(question: String, packageName: String?): AdvisorResponse {
        val q = question.lowercase()

        return when {
            q.contains("mic") || q.contains("microphone") || q.contains("audio") || q.contains("माइक") -> {
                AdvisorResponse(
                    answer = "माइक्रोफोन अनुमति ऐप को आपकी आवाज रिकॉर्ड करने का अधिकार देती है। जब तक कि यह कोई कॉलिंग या वीडियो रिकॉर्डिंग ऐप न हो, बैकग्राउंड में माइक अनुमति देना जोखिम भरा हो सकता है।",
                    recommendedAction = "सेटिंग्स > अनुमतियां > माइक्रोफोन पर जाएं और 'केवल ऐप का उपयोग करते समय अनुमति दें' चुनें।",
                    source = "लोकल एआई नियम इंजन"
                )
            }
            q.contains("location") || q.contains("gps") || q.contains("लोकेशन") -> {
                AdvisorResponse(
                    answer = "बैकग्राउंड लोकेशन एक्सेस ऐप को आपके ऐप बंद करने के बाद भी लगातार ट्रैक करने की अनुमति देता है। नेविगेशन और डिलीवरी ऐप्स के अलावा अन्य ऐप्स के लिए बैकग्राउंड लोकेशन बंद रखें।",
                    recommendedAction = "लोकेशन अनुमति को 'केवल ऐप उपयोग के दौरान' पर सेट करें।",
                    source = "लोकल एआई नियम इंजन"
                )
            }
            q.contains("score") || q.contains("low") || q.contains("high") || q.contains("स्कोर") -> {
                AdvisorResponse(
                    answer = "आपका प्राइवेसी स्कोर ऐप्स को दी गई अनुमतियों, बैकग्राउंड एक्सेस, अज्ञात इंस्टॉलर स्रोत और विशेष एक्सेस (एक्सेसिबिलिटी, ओवरले) के आधार पर निकाला जाता है।",
                    recommendedAction = "अनावश्यक अनुमतियों को बंद करके अपना स्कोर सुधारें।",
                    source = "लोकल जोखिम नियम इंजन"
                )
            }
            q.contains("upi") || q.contains("bank") || q.contains("paytm") || q.contains("phonepe") || q.contains("बैंक") -> {
                AdvisorResponse(
                    answer = "यूपीआई और वित्तीय ऐप्स के साथ स्क्रीन ओवरले ('अन्य ऐप्स के ऊपर प्रदर्शित करें') या एक्सेसिबिलिटी सेवाएं चालू होना जोखिम भरा है। यह आपकी स्क्रीन रिकॉर्ड कर सकते हैं या गलत क्लिक कर सकते हैं।",
                    recommendedAction = "इन वित्तीय ऐप्स के लिए एक्सेसिबिलिटी और ओवरले अनुमतियों की जांच करें।",
                    source = "भारतीय वित्तीय सुरक्षा चेकलिस्ट"
                )
            }
            else -> {
                AdvisorResponse(
                    answer = "प्राइवेसी मॉनिटर आपके डिवाइस पर सभी अनुमतियों और ऐप व्यवहार की निगरानी करता है। किसी भी ऐप की अनुमति बदलने के लिए ऐप विवरण स्क्रीन पर 'अनुमतियों की समीक्षा करें' पर टैप करें।",
                    recommendedAction = "नियमित रूप से पूर्ण प्राइवेसी स्कैन चलाएं।",
                    source = "लोकल प्राइवेसी सलाहकार"
                )
            }
        }
    }
}
