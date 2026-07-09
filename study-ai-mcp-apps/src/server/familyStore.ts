import { persistenceService } from './persistenceService';
import bcrypt from 'bcrypt';

const SALT_ROUNDS = 10;

export interface Family {
  id: string;
  name: string;
  description: string;
  avatar: string | null;
  createdAt: string;
  createdBy: string;
  adminId: string;
}

export interface User {
  id: string;
  email: string;
  password: string;
  nickname: string;
  avatar: string | null;
  phone: string | null;
  createdAt: string;
}

class FamilyStore {
  private families: Family[] = [];
  private users: User[] = [];

  constructor() {
    const savedUsers = persistenceService.getUsers();
    const savedFamilies = persistenceService.getFamilies();

    if (savedUsers.length > 0) {
      this.users = savedUsers;
      this.families = savedFamilies;
    } else {
      this.users.push({
        id: 'user-1',
        email: 'admin@family.com',
        password: '123456',
        nickname: '管理员',
        avatar: null,
        phone: '13800138000',
        createdAt: '2024-01-01',
      });

      this.users.push({
        id: 'user-2',
        email: 'wang@family.com',
        password: '123456',
        nickname: '王家人',
        avatar: null,
        phone: '13900139000',
        createdAt: '2024-01-02',
      });

      this.users.push({
        id: 'user-3',
        email: 'li@family.com',
        password: '123456',
        nickname: '李家人',
        avatar: null,
        phone: '13700137000',
        createdAt: '2024-01-03',
      });

      this.families.push({
        id: 'family-1',
        name: '张氏家族',
        description: '张氏族谱，记录家族成员信息和历史传承，源自河北沧州',
        avatar: null,
        createdAt: '2024-01-01',
        createdBy: 'user-1',
        adminId: 'user-1',
      });

      this.families.push({
        id: 'family-2',
        name: '王氏家族',
        description: '王氏族谱，传承百年家风，祖籍山东济南',
        avatar: null,
        createdAt: '2024-01-02',
        createdBy: 'user-2',
        adminId: 'user-2',
      });

      this.families.push({
        id: 'family-3',
        name: '李氏家族',
        description: '李氏族谱，源远流长的家族文化，源自河南洛阳',
        avatar: null,
        createdAt: '2024-01-03',
        createdBy: 'user-3',
        adminId: 'user-3',
      });

      persistenceService.setUsers(this.users);
      persistenceService.setFamilies(this.families);
    }
  }

  findUserByEmail(email: string): User | undefined {
    return this.users.find(u => u.email === email);
  }

  findUserById(userId: string): User | undefined {
    return this.users.find(u => u.id === userId);
  }

  async verifyPassword(user: User, password: string): Promise<boolean> {
    return bcrypt.compare(password, user.password);
  }

  async createUser(email: string, password: string, nickname: string): Promise<User> {
    const hashedPassword = await bcrypt.hash(password, SALT_ROUNDS);
    const user: User = {
      id: 'user-' + Date.now(),
      email,
      password: hashedPassword,
      nickname,
      avatar: null,
      phone: null,
      createdAt: new Date().toISOString().split('T')[0],
    };
    this.users.push(user);
    persistenceService.setUsers(this.users);
    return user;
  }

  updateUser(userId: string, data: Partial<User>): User | undefined {
    const index = this.users.findIndex(u => u.id === userId);
    if (index === -1) return undefined;
    this.users[index] = {
      ...this.users[index],
      ...data,
    };
    persistenceService.setUsers(this.users);
    return this.users[index];
  }

  async updatePassword(userId: string, oldPassword: string, newPassword: string): Promise<boolean> {
    const index = this.users.findIndex(u => u.id === userId);
    if (index === -1) return false;
    const user = this.users[index];
    const isOldPasswordValid = await bcrypt.compare(oldPassword, user.password);
    if (!isOldPasswordValid) return false;
    const hashedPassword = await bcrypt.hash(newPassword, SALT_ROUNDS);
    this.users[index].password = hashedPassword;
    persistenceService.setUsers(this.users);
    return true;
  }

  getAllFamilies(): Family[] {
    return this.families;
  }

  getFamilyById(familyId: string): Family | undefined {
    return this.families.find(f => f.id === familyId);
  }

  createFamily(name: string, description: string, createdBy: string): Family {
    const family: Family = {
      id: 'family-' + Date.now(),
      name,
      description,
      avatar: null,
      createdAt: new Date().toISOString().split('T')[0],
      createdBy,
      adminId: createdBy,
    };
    this.families.push(family);
    persistenceService.setFamilies(this.families);
    return family;
  }

  updateFamily(familyId: string, name: string, description: string): Family | undefined {
    const index = this.families.findIndex(f => f.id === familyId);
    if (index === -1) return undefined;
    this.families[index] = {
      ...this.families[index],
      name,
      description,
    };
    persistenceService.setFamilies(this.families);
    return this.families[index];
  }

  deleteFamily(familyId: string): boolean {
    const index = this.families.findIndex(f => f.id === familyId);
    if (index === -1) return false;
    this.families.splice(index, 1);
    persistenceService.setFamilies(this.families);
    return true;
  }
}

export const familyStore = new FamilyStore();