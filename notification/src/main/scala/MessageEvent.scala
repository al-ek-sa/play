import java.util.UUID

enum MessageEvent {
  case CREATED(messageID: UUID, recipientId: String, senderId: String, preview: String)
  case DELETED(messageID: UUID)
}