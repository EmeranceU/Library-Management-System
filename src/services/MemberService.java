package services;

import exceptions.MemberNotFoundException;
import models.Member;
import storage.FileManager;

import java.util.Map;

public class MemberService {

    private Map<Integer, Member> members;
    private final FileManager fileManager;

    public MemberService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.members = fileManager.loadMembers();
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
        fileManager.saveMembers(members);
    }

    public Member getMember(int memberId) {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found.");
        }
        return members.get(memberId);
    }

    public boolean memberExists(int memberId) {
        return members.containsKey(memberId);
    }

    public int nextMemberId() {
        return members.isEmpty() ? 1 : members.keySet().stream().max(Integer::compareTo).get() + 1;
    }

    public Map<Integer, Member> getAllMembers() {
        return members;
    }
}
