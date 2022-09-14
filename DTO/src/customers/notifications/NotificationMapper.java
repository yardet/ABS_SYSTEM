package customers.notifications;


import services.customers_service.notification.Notification;

public class NotificationMapper {
    Notification notification;
    NotificationDTO notificatonDTO;

    public NotificationMapper(NotificationDTO notificationDTO) {
        this.notification = null;
        this.notificatonDTO = notificationDTO;
    }

    public NotificationMapper(Notification notification) {
        this.notification = notification;
        this.notificatonDTO = null;
    }

    public NotificationDTO mapToDTO() {
        this.notificatonDTO = new NotificationDTO(this.notification.getLoanName()
                ,this.notification.getYaz(),this.notification.getFinalAmount());
        return this.notificatonDTO;
    }
}
