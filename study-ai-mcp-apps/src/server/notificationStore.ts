import { persistenceService } from './persistenceService';

export type NotificationType = 'invite' | 'like' | 'comment' | 'event' | 'reminder';

export interface Notification {
  id: string;
  userId: string;
  type: NotificationType;
  title: string;
  message: string;
  relatedId: string;
  relatedType: 'family' | 'member' | 'feed' | 'event';
  isRead: boolean;
  createdAt: string;
}

class NotificationStore {
  private notifications: Notification[] = [];

  constructor() {
    const savedNotifications = persistenceService.getNotifications();
    if (savedNotifications.length > 0) {
      this.notifications = savedNotifications;
    }
  }

  getAllNotifications(): Notification[] {
    return this.notifications;
  }

  getNotificationsByUser(userId: string): Notification[] {
    return this.notifications
      .filter(n => n.userId === userId)
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  }

  getUnreadCount(userId: string): number {
    return this.notifications.filter(n => n.userId === userId && !n.isRead).length;
  }

  createNotification(
    userId: string,
    type: NotificationType,
    title: string,
    message: string,
    relatedId: string,
    relatedType: 'family' | 'member' | 'feed' | 'event'
  ): Notification {
    const notification: Notification = {
      id: 'notif-' + Date.now(),
      userId,
      type,
      title,
      message,
      relatedId,
      relatedType,
      isRead: false,
      createdAt: new Date().toISOString().split('T')[0],
    };
    this.notifications.push(notification);
    persistenceService.setNotifications(this.notifications);
    return notification;
  }

  markAsRead(notificationId: string): boolean {
    const index = this.notifications.findIndex(n => n.id === notificationId);
    if (index === -1) return false;
    this.notifications[index].isRead = true;
    persistenceService.setNotifications(this.notifications);
    return true;
  }

  markAllAsRead(userId: string): void {
    this.notifications.forEach(n => {
      if (n.userId === userId) {
        n.isRead = true;
      }
    });
    persistenceService.setNotifications(this.notifications);
  }

  deleteNotification(notificationId: string): boolean {
    const index = this.notifications.findIndex(n => n.id === notificationId);
    if (index === -1) return false;
    this.notifications.splice(index, 1);
    persistenceService.setNotifications(this.notifications);
    return true;
  }

  deleteNotificationsByRelatedId(relatedId: string): void {
    this.notifications = this.notifications.filter(n => n.relatedId !== relatedId);
    persistenceService.setNotifications(this.notifications);
  }
}

export const notificationStore = new NotificationStore();
