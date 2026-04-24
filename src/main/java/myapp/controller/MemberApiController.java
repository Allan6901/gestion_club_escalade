package myapp.controller;

import myapp.model.Member;
import myapp.model.Trip;
import myapp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-trips")
    public ResponseEntity<Member> getMemberWithTrips(@PathVariable Long id) {
        Optional<Member> member = memberService.getMemberWithTrips(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberService.getMemberByEmail(email);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        try {
            Member created = memberService.createMember(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable Long id,
            @RequestBody Member member) {
        Optional<Member> existing = memberService.getMemberById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Member updated = existing.get();
        if (member.getLastName() != null) {
            updated.setLastName(member.getLastName());
        }
        if (member.getFirstName() != null) {
            updated.setFirstName(member.getFirstName());
        }
        if (member.getEmail() != null) {
            updated.setEmail(member.getEmail());
        }
        if (member.getPassword() != null) {
            updated.setPassword(member.getPassword());
        }

        Member result = memberService.updateMember(updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/trips")
    public ResponseEntity<List<Trip>> getTripsByMember(@PathVariable Long id) {
        List<Trip> trips = memberService.getTripsByMember(id);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> countMembers() {
        long count = memberService.getTotalMembers();
        return ResponseEntity.ok(count);
    }
}