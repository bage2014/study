export type EventType = 'birth' | 'death' | 'marriage' | 'graduation' | 'job' | 'move' | 'achievement' | 'other';

export interface FamilyEvent {
  id: string;
  familyId: string;
  memberId: string | null;
  type: EventType;
  title: string;
  description: string;
  eventDate: string;
  createdAt: string;
}

class EventStore {
  private events: FamilyEvent[] = [];

  constructor() {
    this.events.push({
      id: 'event-1',
      familyId: 'family-1',
      memberId: 'member-1',
      type: 'birth',
      title: '张建国出生',
      description: '张建国出生于1950年5月15日',
      eventDate: '1950-05-15',
      createdAt: '2024-01-01',
    });

    this.events.push({
      id: 'event-2',
      familyId: 'family-1',
      memberId: 'member-2',
      type: 'birth',
      title: '李秀英出生',
      description: '李秀英出生于1952年8月20日',
      eventDate: '1952-08-20',
      createdAt: '2024-01-01',
    });

    this.events.push({
      id: 'event-3',
      familyId: 'family-1',
      memberId: null,
      type: 'marriage',
      title: '张建国与李秀英结婚',
      description: '张建国与李秀英喜结连理',
      eventDate: '1973-10-01',
      createdAt: '2024-01-01',
    });

    this.events.push({
      id: 'event-4',
      familyId: 'family-1',
      memberId: 'member-1',
      type: 'death',
      title: '张建国逝世',
      description: '张建国于2020年12月20日逝世',
      eventDate: '2020-12-20',
      createdAt: '2024-01-01',
    });

    this.events.push({
      id: 'event-5',
      familyId: 'family-1',
      memberId: 'member-5',
      type: 'achievement',
      title: '张小明获得奖学金',
      description: '张小明在学校获得年度奖学金',
      eventDate: '2024-06-15',
      createdAt: '2024-01-01',
    });
  }

  getAllEvents(): FamilyEvent[] {
    return this.events;
  }

  getEventsByFamily(familyId: string): FamilyEvent[] {
    return this.events.filter(e => e.familyId === familyId).sort((a, b) => 
      new Date(b.eventDate).getTime() - new Date(a.eventDate).getTime()
    );
  }

  getEventsByMember(memberId: string): FamilyEvent[] {
    return this.events.filter(e => e.memberId === memberId);
  }

  getEventById(eventId: string): FamilyEvent | undefined {
    return this.events.find(e => e.id === eventId);
  }

  createEvent(data: {
    familyId: string;
    memberId: string | null;
    type: EventType;
    title: string;
    description: string;
