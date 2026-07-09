import { persistenceService } from './persistenceService';

export interface Feed {
  id: string;
  familyId: string;
  userId: string;
  userName: string;
  type: 'text' | 'photo' | 'event' | 'announcement';
  content: string;
  photoUrl: string | null;
  relatedMemberId: string | null;
  relatedEventId: string | null;
  createdAt: string;
  likes: number;
  likedBy: string[];
  comments: FeedComment[];
}

export interface FeedComment {
  id: string;
  userId: string;
  userName: string;
  content: string;
  createdAt: string;
}

class FeedStore {
  private feeds: Feed[] = [];

  constructor() {
    const savedFeeds = persistenceService.getFeeds();
    
    if (savedFeeds.length > 0) {
      this.feeds = savedFeeds;
    } else {
      this.feeds.push({
      id: 'feed-1',
      familyId: 'family-1',
      userId: 'user-1',
      userName: '管理员',
      type: 'announcement',
      content: '各位家人，今年的春节聚会定于正月初五在老家举行，请大家提前安排时间。',
      photoUrl: null,
      relatedMemberId: null,
      relatedEventId: null,
      createdAt: '2024-01-10',
      likes: 12,
      likedBy: ['user-2', 'user-3'],
      comments: [
        {
          id: 'comment-1',
          userId: 'user-2',
          userName: '王家人',
          content: '收到，一定准时参加！',
          createdAt: '2024-01-10',
        },
      ],
    });

    this.feeds.push({
      id: 'feed-2',
      familyId: 'family-1',
      userId: 'user-1',
      userName: '管理员',
      type: 'photo',
      content: '整理老照片时发现了这张祖父年轻时的照片，分享给大家',
      photoUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=elderly%20chinese%20man%20portrait%20vintage&image_size=portrait_4_3',
      relatedMemberId: 'member-1',
      relatedEventId: null,
      createdAt: '2024-01-12',
      likes: 8,
      likedBy: [],
      comments: [],
    });

    this.feeds.push({
      id: 'feed-3',
      familyId: 'family-1',
      userId: 'user-2',
      userName: '王芳',
      type: 'text',
      content: '张小明期末考试取得了年级前十名的好成绩，特此表扬！',
      photoUrl: null,
      relatedMemberId: 'member-5',
      relatedEventId: null,
      createdAt: '2024-01-15',
      likes: 15,
      likedBy: ['user-1', 'user-3'],
      comments: [
        {
          id: 'comment-2',
          userId: 'user-1',
          userName: '管理员',
          content: '太棒了！继续加油！',
          createdAt: '2024-01-15',
        },
      ],
    });

    this.feeds.push({
      id: 'feed-4',
      familyId: 'family-2',
      userId: 'user-2',
      userName: '王家人',
      type: 'event',
      content: '祝贺王小强获得公司年度优秀员工称号！',
      photoUrl: null,
      relatedMemberId: 'member-13',
      relatedEventId: null,
      createdAt: '2024-02-05',
      likes: 20,
      likedBy: ['user-1'],
      comments: [],
    });

    this.feeds.push({
      id: 'feed-5',
      familyId: 'family-2',
      userId: 'user-2',
      userName: '王家人',
      type: 'photo',
      content: '家族庆典活动圆满结束，感谢各位家人的参与',
      photoUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20family%20celebration%20group%20photo&image_size=landscape_16_9',
      relatedMemberId: null,
      relatedEventId: null,
      createdAt: '2024-02-01',
      likes: 25,
      likedBy: [],
      comments: [],
    });

    this.feeds.push({
      id: 'feed-6',
      familyId: 'family-3',
      userId: 'user-3',
      userName: '李家人',
      type: 'text',
      content: '李悦在学校的绘画比赛中获得了一等奖！',
      photoUrl: null,
      relatedMemberId: 'member-19',
      relatedEventId: null,
      createdAt: '2024-01-20',
      likes: 18,
      likedBy: ['user-1'],
      comments: [],
    });

      persistenceService.setFeeds(this.feeds);
    }
  }

  getAllFeeds(): Feed[] {
    return this.feeds;
  }

  getFeedsByFamily(familyId: string): Feed[] {
    return this.feeds.filter(f => f.familyId === familyId).sort((a, b) => 
      new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
    );
  }

  getFeedById(feedId: string): Feed | undefined {
    return this.feeds.find(f => f.id === feedId);
  }

  createFeed(data: {
    familyId: string;
    userId: string;
    userName: string;
    type: 'text' | 'photo' | 'event' | 'announcement';
    content: string;
    photoUrl?: string | null;
    relatedMemberId?: string | null;
    relatedEventId?: string | null;
  }): Feed {
    const feed: Feed = {
      id: 'feed-' + Date.now(),
      ...data,
      photoUrl: data.photoUrl || null,
      relatedMemberId: data.relatedMemberId || null,
      relatedEventId: data.relatedEventId || null,
      createdAt: new Date().toISOString().split('T')[0],
      likes: 0,
      likedBy: [],
      comments: [],
    };
    this.feeds.push(feed);
    persistenceService.setFeeds(this.feeds);
    return feed;
  }

  deleteFeed(feedId: string): boolean {
    const index = this.feeds.findIndex(f => f.id === feedId);
    if (index === -1) return false;
    this.feeds.splice(index, 1);
    persistenceService.setFeeds(this.feeds);
    return true;
  }

  toggleLike(feedId: string, userId: string): Feed | undefined {
    const feed = this.getFeedById(feedId);
    if (!feed) return undefined;

    const likeIndex = feed.likedBy.indexOf(userId);
    if (likeIndex === -1) {
      feed.likedBy.push(userId);
      feed.likes++;
    } else {
      feed.likedBy.splice(likeIndex, 1);
      feed.likes--;
    }

    persistenceService.setFeeds(this.feeds);
    return feed;
  }

  addComment(feedId: string, data: {
    userId: string;
    userName: string;
    content: string;
  }): Feed | undefined {
    const feed = this.getFeedById(feedId);
    if (!feed) return undefined;

    const comment: FeedComment = {
      id: 'comment-' + Date.now(),
      ...data,
      createdAt: new Date().toISOString().split('T')[0],
    };
    feed.comments.push(comment);

    persistenceService.setFeeds(this.feeds);
    return feed;
  }

  deleteComment(feedId: string, commentId: string): Feed | undefined {
    const feed = this.getFeedById(feedId);
    if (!feed) return undefined;

    const index = feed.comments.findIndex(c => c.id === commentId);
    if (index === -1) return undefined;

    feed.comments.splice(index, 1);
    persistenceService.setFeeds(this.feeds);
    return feed;
  }
}

export const feedStore = new FeedStore();
