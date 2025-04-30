package usth.intern.notifts.domain.manager

import usth.intern.notifts.data.DatabaseRepository
import javax.inject.Inject

class ManagerSystem @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val searchEngine: SearchEngine,
) {
    /**
     * Save newest notification to database. This function is controlled by onNotificationPosted()
     *
     * @param notificationMap: Containing the information of notification
     */
    suspend fun saveNotification(notificationMap: Map<String, String>) {
        databaseRepository.insertNotification(
            packageName = notificationMap["packageName"] ?: "",
            title = notificationMap["title"] ?: "",
            text = notificationMap["text"],
            bigText = notificationMap["bigText"],
            category = notificationMap["category"],
            date = notificationMap["dateString"] ?: ""
        )
    }

    //TODO
    fun search(type: String) {
//        when (type) {
//            "keyword" -> searchEngine.searchByKeyword()
//        }
    }

    //TODO
    fun filter() {

    }
}