package com.example.fitcontrol.feature_members.domain.use_case

import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.repository.MemberRepository
import javax.inject.Inject

class SaveMember @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(member: Member) {
        repository.insertMember(member)
    }
}