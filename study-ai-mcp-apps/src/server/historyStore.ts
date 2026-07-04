export interface HistoryEvent {
  id: string;
  familyId: string;
  type: 'event' | 'milestone' | 'log';
  title: string;
  description: string;
  date: string;
  relatedMemberId: string | null;
  operator: string | null;
}

class HistoryStore {
  private events: HistoryEvent[] = [];

  constructor() {
    this.events.push({
      id: 'evt-1',
      familyId: 'family-1',
      type: 'event',
      title: '家族成立',
      description: '张氏家族正式成立，开始记录家族成员信息',
      date: '2024-01-01',
      relatedMemberId: null,
      operator: '管理员',
    });

    this.events.push({
      id: 'evt-2',
      familyId: 'family-1',
      type: 'milestone',
      title: '张建国出生',
      description: '张建国出生于1950年5月15日',
      date: '1950-05-15',
      relatedMemberId: 'member-1',
      operator: null,
    });

    this.events.push({
      id: 'evt-3',
      familyId: 'family-1',
      type: 'milestone',
      title: '李秀英出生',
      description: '李秀英出生于1952年8月20日',
      date: '1952-08-20',
      relatedMemberId: 'member-2',
      operator: null,
    });

    this.events.push({
      id: 'evt-4',
      familyId: 'family-1',
      type: 'event',
      title: '张建国与李秀英结婚',
      description: '张建国与李秀英喜结连理',
      date: '1972-10-01',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-5',
      familyId: 'family-1',
      type: 'milestone',
      title: '张伟出生',
      description: '张建国长子张伟出生',
      date: '1975-03-10',
      relatedMemberId: 'member-3',
      operator: null,
    });

    this.events.push({
      id: 'evt-6',
      familyId: 'family-1',
      type: 'milestone',
      title: '张丽出生',
      description: '张建国之女张丽出生',
      date: '1980-09-05',
      relatedMemberId: 'member-6',
      operator: null,
    });

    this.events.push({
      id: 'evt-7',
      familyId: 'family-1',
      type: 'event',
      title: '张伟与王芳结婚',
      description: '张伟与王芳喜结连理',
      date: '1998-05-01',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-8',
      familyId: 'family-1',
      type: 'milestone',
      title: '张小明出生',
      description: '张伟之子张小明出生',
      date: '2005-11-20',
      relatedMemberId: 'member-5',
      operator: null,
    });

    this.events.push({
      id: 'evt-9',
      familyId: 'family-1',
      type: 'event',
      title: '张丽与李明结婚',
      description: '张丽与李明喜结连理',
      date: '2002-06-15',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-10',
      familyId: 'family-1',
      type: 'milestone',
      title: '李婷婷出生',
      description: '张丽之女李婷婷出生',
      date: '2010-07-08',
      relatedMemberId: 'member-8',
      operator: null,
    });

    this.events.push({
      id: 'evt-11',
      familyId: 'family-1',
      type: 'event',
      title: '张建国去世',
      description: '张建国于2020年12月20日去世',
      date: '2020-12-20',
      relatedMemberId: 'member-1',
      operator: null,
    });

    this.events.push({
      id: 'log-1',
      familyId: 'family-1',
      type: 'log',
      title: '管理员创建家族',
      description: '管理员创建了张氏家族',
      date: '2024-01-01 10:00:00',
      relatedMemberId: null,
      operator: '管理员',
    });

    this.events.push({
      id: 'log-2',
      familyId: 'family-1',
      type: 'log',
      title: '添加成员',
      description: '添加成员张建国',
      date: '2024-01-01 10:05:00',
      relatedMemberId: 'member-1',
      operator: '管理员',
    });

    this.events.push({
      id: 'log-3',
      familyId: 'family-1',
      type: 'log',
      title: '添加关系',
      description: '添加张建国与李秀英的夫妻关系',
      date: '2024-01-01 10:10:00',
      relatedMemberId: null,
      operator: '管理员',
    });

    this.events.push({
      id: 'evt-12',
      familyId: 'family-2',
      type: 'event',
      title: '家族成立',
      description: '王氏家族正式成立，传承百年家风',
      date: '2024-01-02',
      relatedMemberId: null,
      operator: '王家人',
    });

    this.events.push({
      id: 'evt-13',
      familyId: 'family-2',
      type: 'milestone',
      title: '王明德出生',
      description: '王明德出生于1945年3月10日',
      date: '1945-03-10',
      relatedMemberId: 'member-9',
      operator: null,
    });

    this.events.push({
      id: 'evt-14',
      familyId: 'family-2',
      type: 'milestone',
      title: '陈桂兰出生',
      description: '陈桂兰出生于1947年7月22日',
      date: '1947-07-22',
      relatedMemberId: 'member-10',
      operator: null,
    });

    this.events.push({
      id: 'evt-15',
      familyId: 'family-2',
      type: 'event',
      title: '王明德与陈桂兰结婚',
      description: '王明德与陈桂兰喜结连理',
      date: '1968-05-01',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-16',
      familyId: 'family-2',
      type: 'milestone',
      title: '王伟出生',
      description: '王明德长子王伟出生',
      date: '1970-05-18',
      relatedMemberId: 'member-11',
      operator: null,
    });

    this.events.push({
      id: 'evt-17',
      familyId: 'family-2',
      type: 'milestone',
      title: '王丽出生',
      description: '王明德之女王丽出生',
      date: '1975-02-14',
      relatedMemberId: 'member-14',
      operator: null,
    });

    this.events.push({
      id: 'evt-18',
      familyId: 'family-2',
      type: 'event',
      title: '王伟与刘燕结婚',
      description: '王伟与刘燕喜结连理',
      date: '1995-10-01',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-19',
      familyId: 'family-2',
      type: 'milestone',
      title: '王小强出生',
      description: '王伟之子王小强出生',
      date: '1995-08-25',
      relatedMemberId: 'member-13',
      operator: null,
    });

    this.events.push({
      id: 'evt-20',
      familyId: 'family-2',
      type: 'event',
      title: '王明德去世',
      description: '王明德于2018年9月15日去世',
      date: '2018-09-15',
      relatedMemberId: 'member-9',
      operator: null,
    });

    this.events.push({
      id: 'log-4',
      familyId: 'family-2',
      type: 'log',
      title: '创建家族',
      description: '王家人创建了王氏家族',
      date: '2024-01-02 09:00:00',
      relatedMemberId: null,
      operator: '王家人',
    });

    this.events.push({
      id: 'evt-21',
      familyId: 'family-3',
      type: 'event',
      title: '家族成立',
      description: '李氏家族正式成立，源远流长的家族文化',
      date: '2024-01-03',
      relatedMemberId: null,
      operator: '李家人',
    });

    this.events.push({
      id: 'evt-22',
      familyId: 'family-3',
      type: 'milestone',
      title: '李祥出生',
      description: '李祥出生于1952年6月15日',
      date: '1952-06-15',
      relatedMemberId: 'member-15',
      operator: null,
    });

    this.events.push({
      id: 'evt-23',
      familyId: 'family-3',
      type: 'milestone',
      title: '赵秀芬出生',
      description: '赵秀芬出生于1954年9月30日',
      date: '1954-09-30',
      relatedMemberId: 'member-16',
      operator: null,
    });

    this.events.push({
      id: 'evt-24',
      familyId: 'family-3',
      type: 'event',
      title: '李祥与赵秀芬结婚',
      description: '李祥与赵秀芬喜结连理',
      date: '1978-03-10',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-25',
      familyId: 'family-3',
      type: 'milestone',
      title: '李刚出生',
      description: '李祥长子李刚出生',
      date: '1980-04-10',
      relatedMemberId: 'member-17',
      operator: null,
    });

    this.events.push({
      id: 'evt-26',
      familyId: 'family-3',
      type: 'milestone',
      title: '李勇出生',
      description: '李祥次子李勇出生',
      date: '1985-03-20',
      relatedMemberId: 'member-20',
      operator: null,
    });

    this.events.push({
      id: 'evt-27',
      familyId: 'family-3',
      type: 'event',
      title: '李刚与孙静结婚',
      description: '李刚与孙静喜结连理',
      date: '2005-07-18',
      relatedMemberId: null,
      operator: null,
    });

    this.events.push({
      id: 'evt-28',
      familyId: 'family-3',
      type: 'milestone',
      title: '李悦出生',
      description: '李刚之女李悦出生',
      date: '2008-12-05',
      relatedMemberId: 'member-19',
      operator: null,
    });

    this.events.push({
      id: 'log-5',
      familyId: 'family-3',
      type: 'log',
      title: '创建家族',
      description: '李家人创建了李氏家族',
      date: '2024-01-03 11:00:00',
      relatedMemberId: null,
      operator: '李家人',
    });
  }

  getAllEvents(): HistoryEvent[] {
    return this.events;
  }

  getEventsByFamily(familyId: string): HistoryEvent[] {
    return this.events.filter(e => e.familyId === familyId);
  }

  getEventById(eventId: string): HistoryEvent | undefined {
    return this.events.find(e => e.id === eventId);
  }

  createEvent(data: {
    familyId: string;
    type: 'event' | 'milestone' | 'log';
    title: string;
    description: string;
    date: string;
    relatedMemberId: string | null;
    operator: string | null;
  }): HistoryEvent {
    const event: HistoryEvent = {
      id: 'evt-' + Date.now(),
      ...data,
    };
    this.events.push(event);
    return event;
  }

  deleteEvent(eventId: string): boolean {
    const index = this.events.findIndex(e => e.id === eventId);
    if (index === -1) return false;
    this.events.splice(index, 1);
    return true;
  }
}

export const historyStore = new HistoryStore();
