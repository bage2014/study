import fs from 'fs';
import path from 'path';

const DATA_DIR = path.join(__dirname, '../../data');

interface PersistedData {
  families: any[];
  users: any[];
  members: any[];
  relationships: any[];
  events: any[];
  albums: any[];
  photos: any[];
  feeds: any[];
  memorials: any[];
  permissions: any[];
  invitations: any[];
}

export class PersistenceService {
  private data: PersistedData = {
    families: [],
    users: [],
    members: [],
    relationships: [],
    events: [],
    albums: [],
    photos: [],
    feeds: [],
    memorials: [],
    permissions: [],
    invitations: [],
  };

  private initialized = false;
  private saveInterval: NodeJS.Timeout | null = null;

  constructor() {
    this.ensureDataDir();
  }

  private ensureDataDir() {
    if (!fs.existsSync(DATA_DIR)) {
      fs.mkdirSync(DATA_DIR, { recursive: true });
    }
  }

  async init() {
    if (this.initialized) return;
    
    await this.loadData();
    this.startAutoSave();
    this.setupShutdownHandler();
    this.initialized = true;
  }

  private async loadData() {
    const filePath = path.join(DATA_DIR, 'data.json');
    if (fs.existsSync(filePath)) {
      try {
        const content = await fs.promises.readFile(filePath, 'utf-8');
        this.data = JSON.parse(content);
        console.log('数据加载成功');
      } catch (error) {
        console.error('数据加载失败，使用默认数据:', error);
      }
    } else {
      console.log('数据文件不存在，使用默认数据');
    }
  }

  async saveData() {
    const filePath = path.join(DATA_DIR, 'data.json');
    try {
      await fs.promises.writeFile(filePath, JSON.stringify(this.data, null, 2));
      console.log('数据保存成功');
    } catch (error) {
      console.error('数据保存失败:', error);
    }
  }

  private startAutoSave() {
    this.saveInterval = setInterval(() => {
      this.saveData();
    }, 30000);
  }

  private setupShutdownHandler() {
    const shutdown = async () => {
      if (this.saveInterval) {
        clearInterval(this.saveInterval);
      }
      await this.saveData();
      process.exit(0);
    };

    process.on('SIGINT', shutdown);
    process.on('SIGTERM', shutdown);
    process.on('beforeExit', shutdown);
  }

  setFamilies(families: any[]) {
    this.data.families = families;
  }

  getFamilies(): any[] {
    return this.data.families;
  }

  setUsers(users: any[]) {
    this.data.users = users;
  }

  getUsers(): any[] {
    return this.data.users;
  }

  setMembers(members: any[]) {
    this.data.members = members;
  }

  getMembers(): any[] {
    return this.data.members;
  }

  setRelationships(relationships: any[]) {
    this.data.relationships = relationships;
  }

  getRelationships(): any[] {
    return this.data.relationships;
  }

  setEvents(events: any[]) {
    this.data.events = events;
  }

  getEvents(): any[] {
    return this.data.events;
  }

  setAlbums(albums: any[]) {
    this.data.albums = albums;
  }

  getAlbums(): any[] {
    return this.data.albums;
  }

  setPhotos(photos: any[]) {
    this.data.photos = photos;
  }

  getPhotos(): any[] {
    return this.data.photos;
  }

  setFeeds(feeds: any[]) {
    this.data.feeds = feeds;
  }

  getFeeds(): any[] {
    return this.data.feeds;
  }

  setMemorials(memorials: any[]) {
    this.data.memorials = memorials;
  }

  getMemorials(): any[] {
    return this.data.memorials;
  }

  setPermissions(permissions: any[]) {
    this.data.permissions = permissions;
  }

  getPermissions(): any[] {
    return this.data.permissions;
  }

  setInvitations(invitations: any[]) {
    this.data.invitations = invitations;
  }

  getInvitations(): any[] {
    return this.data.invitations;
  }
}

export const persistenceService = new PersistenceService();
